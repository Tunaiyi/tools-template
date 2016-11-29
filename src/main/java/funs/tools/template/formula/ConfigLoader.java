package funs.tools.template.formula;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConfigLoader {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);

	public static File loadFile(String path) {
		final String configPath = path;
		LOG.info("#ConfigLoader#打开 {} ", path);
		final URL url = Thread.currentThread().getContextClassLoader().getResource(configPath);
		if (url == null) {
			LOG.warn("#ConfigLoader#打开 {} 失败", path);
			return null;
		}
		//		System.out.println(url.getPath());
		return new File(url.getPath());
	}

	public static InputStream loadInputStream(String path) throws IOException {
		final String configPath = path;
		LOG.info("#ConfigLoader#打开 {} ", path);
		final URL url = Thread.currentThread().getContextClassLoader().getResource(configPath);
		if (url == null) {
			LOG.warn("#ConfigLoader#打开 {} 失败", path);
			throw new FileNotFoundException(MessageFormat.format("{0} 文件不存在", path));
		}
		InputStream inputStream = null;
		try {
			inputStream = url.openStream();
			inputStream = new BufferedInputStream(inputStream);
			LOG.info("#ConfigLoader#打开 {} 成功", url);
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			if (inputStream != null)
				inputStream.close();
			return null;
		}
		return inputStream;
	}

}
