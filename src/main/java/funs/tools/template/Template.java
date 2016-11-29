package funs.tools.template;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;

import funs.tools.template.util.FormulaUtils;
import funs.tools.template.util.MathEx;

public class Template {

	private CompiledTemplate expression;

	public final static ThreadLocal<ParserContext> PARSER_CONTEXT_LOCAL = new ThreadLocal<ParserContext>() {

		@Override
		protected ParserContext initialValue() {
			ParserContext context = new ParserContext();
			for (final Method method : Math.class.getMethods())
				context.addImport(method.getName(), method);
			for (final Method method : MathEx.class.getMethods())
				context.addImport(method.getName(), method);
			for (final Method method : FormulaUtils.class.getMethods())
				context.addImport(method.getName(), method);
			context.addImport(ArrayList.class);
			context.addImport(HashSet.class);
			context.addImport(HashMap.class);
			return context;
		}

	};

	public static void main(String[] args) {
		Serializable value = MVEL.compileExpression("ufl('abcDefGhij')", PARSER_CONTEXT_LOCAL.get());
		System.out.println(MVEL.executeExpression(value));
	}

	public static Template load(String templatePath) throws Exception {
		InputStream in = null;
		String template = null;
		try {
			in = new FileInputStream(templatePath);
			template = IOUtils.toString(in, GameConstant.ENCODING);
			return new Template(template);
		} catch (FileNotFoundException e) {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
			if (in == null)
				throw e;
		} finally {
			IOUtils.closeQuietly(in);
		}
		return null;
	}

	public static Template load(InputStream in) throws Exception {
		String template = IOUtils.toString(in, GameConstant.ENCODING);
		return new Template(template);
	}
	
	public static Template of(String template) throws Exception {
		return new Template(template);
	}

	private Template(String template) {
		expression = TemplateCompiler.compileTemplate(template, PARSER_CONTEXT_LOCAL.get());
	}

	public String getContent(Map<String, Object> attributes) {
		return TemplateRuntime.execute(expression, attributes).toString();
	}

	public InputStream getContentStream(Map<String, Object> attributes) throws Exception {
		String template = getContent(attributes);
		return new ByteArrayInputStream(template.getBytes(GameConstant.ENCODING));
	}

}
