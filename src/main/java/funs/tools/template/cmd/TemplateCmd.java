package funs.tools.template.cmd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import funs.tools.template.ConfigFileUtil;
import funs.tools.template.excel.SheetFilter;
import funs.tools.template.excel.TemplateSheetManager;

public class TemplateCmd {

	private static SheetFilter filter = new SheetFilter() {

		@Override
		public boolean accept(String sheetName) {
			if (sheetName.equals(TemplateSheetManager.GLOBAL_SHEET_NAME))
				return true;
			else if ((sheetName.startsWith("s.") || sheetName.startsWith("c.")) && sheetName.endsWith("mvl"))
				return true;
			return false;
		}

	};

	private static final String BASE_PATH_KEY = "-basepath";

	public static void main(String[] args) throws IOException {
		//		args = new String[] { "//Users//funsplay//Desktop//dto//DemandResultListDTO.xml" };
		List<String> fileList = new ArrayList<String>();
		Properties properties = new Properties();
		for (String arg : args) {
			if (arg.startsWith("-")) {
				String[] property = arg.split("=");
				if (property.length < 2)
					throw new IllegalArgumentException(arg + " is error!");
				properties.put(property[0], property[1]);
			} else {
				fileList.add(arg);
			}
		}
		for (String filePath : args) {
			try {
				ConfigFileUtil.dealConfigFile(new File(filePath), properties.getProperty(BASE_PATH_KEY), filter);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("任意健关闭");
		System.in.read(new byte[1]);
		System.exit(0);
	}
}
