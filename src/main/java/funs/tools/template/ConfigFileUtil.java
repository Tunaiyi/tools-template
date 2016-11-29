package funs.tools.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mvel2.ParserContext;

import funs.tools.template.excel.SheetFilter;
import funs.tools.template.excel.TemplateSheetManager;

public class ConfigFileUtil {

	/**
	 * 模板后缀
	 */
	// private static final String TMP_SUFFIX = ".mvl";

	private static final String logFileName = "log.txt";

	protected ParserContext parserContext = new ParserContext();

	public static void dealConfigFile(File config, String basePath, SheetFilter filter) throws Exception {
		InputStream in = null;
		File dir = basePath != null ? new File(basePath) : config.getParentFile();
		List<String> errorList = new ArrayList<>();
		try {
			in = new FileInputStream(config);
			SheetSrcType type = SheetSrcType.getByFile(config);
			TemplateSheetManager excelManager = new TemplateSheetManager(in, type, filter);
			TemplateSheet paramSheet = excelManager.getTemplateSheetBy(TemplateSheetManager.GLOBAL_SHEET_NAME);
			for (TemplateSheet sheet : excelManager.getTemplateSheetList()) {
				if (!sheet.needOutPut())
					continue;
				List<String> profiles = sheet.getProfiles();
				String mvlFilePath = dir.getAbsolutePath() + sheet.getMvl();
				String outputPath = dir.getAbsolutePath() + sheet.getOutput();
				if (profiles.isEmpty()) {
					outputByProfile(config, sheet, paramSheet, mvlFilePath, outputPath, "", errorList);
				} else {
					for (String profile : profiles)
						outputByProfile(config, sheet, paramSheet, mvlFilePath, outputPath, profile, errorList);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			log(e);
			throw e;
		} finally {
			IOUtils.closeQuietly(in);
			if (!errorList.isEmpty()) {
				throw new TemplateException(errorList.toString());
			}
		}
	}

	private static void outputByProfile(File config, TemplateSheet sheet, TemplateSheet paramSheet, String mvlFilePath,
			String outputPath, String profile, List<String> errorList) {
		try {
			Map<String, Object> attributeMap = new HashMap<>(sheet.getAttributeMap(profile));
			mvlFilePath = Template.of(mvlFilePath).getContent(attributeMap);
			outputPath = Template.of(outputPath).getContent(attributeMap);
			Template template = Template.load(mvlFilePath);
			if (paramSheet != null)
				attributeMap.putAll(paramSheet.getAttributeMap(profile));
			File outputFile = new File(outputPath);
			try (InputStream contentStream = template.getContentStream(attributeMap);
					BufferedReader reader = new BufferedReader(new InputStreamReader(contentStream));
					OutputStream xmlStream = FileUtils.openOutputStream(outputFile)) {
				String line = "";
				while ((line = reader.readLine()) != null) {
					if (!line.trim().isEmpty()) {
						IOUtils.write(line + "\r\n", xmlStream);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			errorList.add("处理Excel文件[" + config.getName() + "]的子表[" + sheet.getName() + "]出错，错误：\n" + e.getMessage());
			log(e);
		}
	}

	private static void log(Throwable e) {
		PrintStream print = null;
		try {
			print = new PrintStream(new FileOutputStream(logFileName, true), false, GameConstant.ENCODING);
			e.printStackTrace(print);
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(print);
		}
	}

	// private static String convertPath(String sheetName) {
	// String[] parts = StringUtils.split(sheetName, '.');
	// if (parts.length == 2) {
	// return sheetName;
	// }
	// String temp = StringUtils.join(Arrays.copyOf(parts, parts.length - 1),
	// '/');
	// return temp + "." + parts[parts.length - 1];
	// }
}
