package com.aventstack.extentreports.reporter.configuration;

import java.io.File;
import java.util.stream.Stream;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.offline.OfflineResxDelegate;
import com.aventstack.extentreports.reporter.BasicFileReporter;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.utils.FileUtil;

/**
 * Defines configuration settings for the Spark reporter
 */
public class ExtentHtmlReporterConfiguration extends RichViewReporterConfiguration {

	public ExtentHtmlReporterConfiguration(ExtentHtmlReporter reporter) {
		super(reporter);
	}

	/**
	 * Creates the HTML report, saving all resources (css, js, fonts) in the same
	 * location, so the report can be viewed without an internet connection
	 * 
	 * @param offline Setting to enable an offline accessible report
	 */
	public void enableOfflineMode(Boolean offline) {
		getConfigurationStore().storeConfig("enableOfflineMode", String.valueOf(offline));
		getConfigurationStore().storeConfig("offlineDirectory", getReporter().getReporterName() + "/");
		if (offline) {
			File f = getTargetDirectory(((BasicFileReporter) getReporter()).getFileFile());
			String s = "/";
			String resourcePackagePath = ExtentReports.class.getPackage().getName().replace(".", s);
			resourcePackagePath += s + "offline" + s;
			String[] resx = combine(getJSFiles(), getCSSFiles(), getIconFiles(), getImgFiles());
			OfflineResxDelegate.saveOfflineResources(resourcePackagePath, resx, f.getAbsolutePath());
		}
	}

	private File getTargetDirectory(File f) {
		String dir;
		if (FileUtil.isDirectory(f)) {
			dir = f.getAbsolutePath().replace("\\", "/");
		} else {
			dir = f.getAbsolutePath().replace("\\", "/");
			dir = new File(dir).getParent();
		}
		dir += "/" + getReporter().getReporterName();
		return new File(dir);
	}

	private String[] combine(String[]... array) {
		String[] result = new String[] {};
		for (String[] arr : array) {
			result = Stream.of(result, arr).flatMap(Stream::of).toArray(String[]::new);
		}
		return result;
	}

	private String[] getJSFiles() {
		String commonsPath = "commons/js/";
		String reporterPath = getReporter().getReporterName() + "/js/";
		String[] files = { reporterPath + "spark-script.js", commonsPath + "jsontree.js" };
		return files;
	}

	private String[] getCSSFiles() {
		String stylesPath = "css/";
		String reporterPath = getReporter().getReporterName() + "/" + stylesPath;
		String[] files = { reporterPath + "spark-style.css" };
		return files;
	}

	private String[] getIconFiles() {
		String path = "commons/css/icons/";
		String iconDirPath = "fontawesome/";
		String[] files = { path + "font-awesome.min.css", path + iconDirPath + "fontawesome-webfont.eot",
				path + iconDirPath + "fontawesome-webfont.svg", path + iconDirPath + "fontawesome-webfont.ttf",
				path + iconDirPath + "fontawesome-webfont.woff", path + iconDirPath + "fontawesome-webfont.woff2",
				path + iconDirPath + "FontAwesome.otf" };
		return files;
	}

	private String[] getImgFiles() {
		String path = "commons/img/";
		String[] files = { path + "logo.png" };
		return files;
	}

}