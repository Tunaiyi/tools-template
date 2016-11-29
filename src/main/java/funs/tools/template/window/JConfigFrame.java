package funs.tools.template.window;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;

import funs.tools.template.ConfigFileUtil;
import funs.tools.template.excel.SheetFilter;
import funs.tools.template.excel.TemplateSheetManager;

public class JConfigFrame extends JFrame implements ActionListener {
	/**
	 *
	 */
	private static final long serialVersionUID = 9116374978294368007L;
	private int width = 300;
	private int height = 100;
	private JButton selectConfigBt;
	private JButton quickBt;
	private FileDialog configChooser = new FileDialog(this, "导出配置文件", FileDialog.LOAD);
	private FileDialog dirChooser = new FileDialog(this, "导出配置文件所在目录的所有文件", FileDialog.LOAD);;

	private SheetFilter filter = new SheetFilter() {

		@Override
		public boolean accept(String sheetName) {
			if (sheetName.equals(TemplateSheetManager.GLOBAL_SHEET_NAME))
				return true;
			else if ((sheetName.startsWith("s.") || sheetName.startsWith("c.")) && sheetName.endsWith("mvl"))
				return true;
			return false;
		}

	};
	private FilenameFilter nameFilter = new FilenameFilter() {

		List<String> exset = Arrays.asList(new String[] { "excel", "xls", "xlsx", "xml", "link" });

		@Override
		public boolean accept(File paramFile, String paramString) {
			String exname = FilenameUtils.getExtension(paramString);
			if (this.exset.contains(exname))
				return true;
			return false;
		}

	};

	//	private FileView fileView = new FileView() {
	//
	//		public boolean isDirLink(File file) {
	//			if (file.getName().toLowerCase().endsWith(".lnk")) {
	//				return true;
	//			}
	//			return false;
	//		}
	//
	//		public Boolean isTraversable(File file) {
	//			if (isDirLink(file))
	//				return Boolean.valueOf(true);
	//			return null;
	//		}
	//
	//	};

	private File configFile;

	private static final FileFilter fileFilter = new SuffixFileFilter(new String[] { "xls", "xlsx", "xml" });

	public JConfigFrame(String title) {
		this(title, ".");
	}

	public JConfigFrame(String title, String rootPath) {
		this.setTitle(title);
		this.setBounds(0, 0, this.width, this.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(new BorderLayout(0, 0));

		//		FileNameExtensionFilter excelFilter = new FileNameExtensionFilter("excel", "xls", "xlsx", "xml");

		this.configChooser.setDirectory(rootPath);
		this.configChooser.setMode(FileDialog.LOAD);
		this.configChooser.setFilenameFilter(this.nameFilter);

		this.dirChooser.setDirectory(rootPath);
		this.dirChooser.setMode(FileDialog.LOAD);
		this.dirChooser.setFilenameFilter(this.nameFilter);

		//		dirChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		//		configChooser.setFileView(fileView);

		this.selectConfigBt = new JButton("选择数据文件");
		this.selectConfigBt.addActionListener(this);

		this.quickBt = new JButton("批量处理");
		this.quickBt.addActionListener(this);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.selectConfigBt);
		buttonPanel.add(this.quickBt);

		this.getContentPane().add(buttonPanel, BorderLayout.PAGE_START);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.selectConfigBt) {
			this.configChooser.setVisible(true);
			String file = this.configChooser.getFile();
			if (file == null)
				return;
			this.configFile = new File(this.configChooser.getDirectory(), file);
			this.dealConfigFile(this.configFile);
			this.tipSuccess();
		} else if (e.getSource() == this.quickBt) {
			this.dirChooser.setVisible(true);
			String file = this.dirChooser.getFile();
			if (file == null)
				return;
			String dir = this.dirChooser.getDirectory();
			if (dir == null)
				return;
			File dirFile = new File(dir);
			this.produceConfigFile(dirFile);
			this.tipSuccess();
		}

	}

	private void tipSuccess() {
		JOptionPane.showMessageDialog(this,
				"处理完成", "提示",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private void dealConfigFile(File config) {
		try {
			ConfigFileUtil.dealConfigFile(config, null, this.filter);
		} catch (Throwable e1) {
			JOptionPane.showMessageDialog(this,
					"导出\n" + config + "\n处理出错，错误：\n" + e1 + " : " + e1.getMessage(), "出错提示",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 批量生成配置文件
	 *
	 * @param dir
	 */
	private void produceConfigFile(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				//				produceConfigFile(file);
			} else if (fileFilter.accept(file)) {
				this.dealConfigFile(file);
			}

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0)
			new JConfigFrame("配置生成器");
		if (args.length == 1)
			new JConfigFrame(args[0]);
		if (args.length == 2)
			new JConfigFrame(args[0], args[1]);
	}

}
