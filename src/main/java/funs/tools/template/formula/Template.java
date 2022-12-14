package funs.tools.template.formula;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.mvel2.ParserContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.mvel2.util.MethodStub;

/**
 * @ClassName: Expression
 * @Description: 表达式包装类
 * @author KGTny
 * @date 2011-10-25 上午1:32:48
 * 
 *       表达式包装类
 *       <p>
 *       <br>
 */
class Template extends AbstractMvelFormula {

	/**
	 * 表达式
	 * 
	 * @uml.property name="expression"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private volatile CompiledTemplate expression;
	/**
	 * @uml.property name="expressionStr"
	 */
	private final String expressionStr;

	protected Template(final String expression, Map<String, Object> context, boolean lazy) {
		this.expressionStr = expression;
		if (this.parserContext == null)
			this.parserContext = new ParserContext();
		if (context != null) {
			if (context != null) {
				for (Entry<String, Object> entry : context.entrySet()) {
					Object value = entry.getValue();
					if (value instanceof Class)
						this.parserContext.addImport(entry.getKey(), (Class<?>) value);
					if (value instanceof Method)
						this.parserContext.addImport(entry.getKey(), (Method) value);
					if (value instanceof MethodStub)
						this.parserContext.addImport(entry.getKey(), (MethodStub) value);
				}
			}
		}
		this.init(this.parserContext);
		this.expression = lazy ? null : this.getExpression();
	}
	

	protected Template(final String expression, ParserContext context, boolean lazy) {
		this.expressionStr = expression;
		if (context != null) {
			this.parserContext = context;
		} else {
			this.parserContext = new ParserContext();
		}
		this.init(this.parserContext);
		this.expression = lazy ? null : this.getExpression();
	}
	


	private Template(final Template template) {
		this.expression = template.getExpression();
		this.expressionStr = template.expressionStr;
	}

	private CompiledTemplate getExpression() {
		if (this.expression == null) {
			synchronized (this) {
				if (this.expression != null) {
					return this.expression;
				}
				for (final Method method : methodSet) {
					this.parserContext.addImport(method.getName(), method);
				}
				this.parserContext.addImport(ArrayList.class);
				this.parserContext.addImport(HashSet.class);
				this.parserContext.addImport(HashMap.class);
				this.expression = TemplateCompiler.compileTemplate(this.expressionStr, this.parserContext);
				this.parserContext = null;
			}
		}
		return this.expression;
	}

	@Override
	public Formula createFormula() {
		return new Template(this);
	}

	@Override
	protected Object execute() {
		try {
			return TemplateRuntime.execute(this.getExpression(), this.attribute);
		} catch (Exception e) {
			throw new RuntimeException("执行 [" + this.expressionStr + "] 异常 ", e);
		}
	}

}
