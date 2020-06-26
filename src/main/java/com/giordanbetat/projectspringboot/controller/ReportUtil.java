package com.giordanbetat.projectspringboot.controller;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] generateReport(List list, String report, ServletContext context) throws Exception {

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);

		String jasper = context.getRealPath("reports") + File.separator + report + ".jasper";

		JasperPrint print = JasperFillManager.fillReport(jasper, new HashMap(), dataSource);

		return JasperExportManager.exportReportToPdf(print);

	}

}
