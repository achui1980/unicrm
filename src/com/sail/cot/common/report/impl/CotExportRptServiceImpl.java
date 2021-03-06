/**
 * 
 */
package com.sail.cot.common.report.impl;

import java.io.File;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.sail.cot.common.exception.ServiceException;
import com.sail.cot.common.report.CotExportRptService;
import com.sail.cot.common.util.ContextUtil;
import com.sail.cot.common.util.ExceptionStackTracePaser;
import com.sail.cot.common.util.Log4WebUtil;
import com.sail.cot.constants.Constants;

/**
 * <p>Title: 旗航不锈钢管理系统（QHERP）</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: 厦门市旗航软件有限公司</p>
 * <p>Create Time: Mar 21, 2011 9:20:02 AM </p>
 * <p>Class Name: CotExportRptServiceImpl.java </p>
 * @author achui
 *
 */
public class CotExportRptServiceImpl extends HibernateDaoSupport implements CotExportRptService{

	private final String XLS_TYPE = "EXCEL";
	private final String PDF_TYPE = "PDF";
	private final String HTML_TYPE = "HTML";
	// 默认报表保存路径
	private String defaultRptXMLPath = "";
	// 默认报表输出路径
	private String defaultExportPath = "";
	private Logger logger = Log4WebUtil.getLogger(CotExportRptServiceImpl.class);
	private static Connection conn = null;
	
	/**
	 * @param rptXMLPath
	 *            报表XML文件路径
	 * @param exportPath
	 *            报表导出路径
	 * @param paramMap
	 *            参数
	 * @param conn
	 *            数据库连接
	 * @return int
	 * @throws JRException
	 */
	private int exportRpt(String rptXMLPath, String exportPath,
			HashMap paramMap, Connection connnection, String type)
			throws JRException {
		// conn = super.getSession().connection();
		int res = -1;
		JasperReport jasperReport;
		JasperPrint jasperPrint = this.getJasperPrint(rptXMLPath, paramMap);
		if (XLS_TYPE.equals(type)) {
			// jasperReport = JasperCompileManager.compileReport(rptXMLPath);
			// jasperPrint =
			// JasperFillManager.fillReport(jasperReport,paramMap,conn);
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					exportPath);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);

			String exlSheet = paramMap.get("exlSheet").toString();
			List pages = jasperPrint.getPages();
			JRPrintText text = null;
			String[] sheetNames = new String[pages.size()];
			for (int i = 0; i < pages.size(); i++) {
				JRPrintPage printPage = (JRPrintPage) pages.get(i);
				for (int j = 0; j < printPage.getElements().size(); j++) {
					JRPrintElement printele = (JRPrintElement) printPage
							.getElements().get(j);
					if (printele instanceof JRPrintText) {
						text = (JRPrintText) printele;
						if (printele.getKey().equals("ele_detail")) {
							System.out.println("Text:" + text.getText());
							sheetNames[i] = text.getText();
							break;
						}
					}
				}
			}
			if (exlSheet.equals("true") && sheetNames.length > 0) {
				exporter.setParameter(
						JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
						Boolean.TRUE);
				exporter.setParameter(
						JRXlsAbstractExporterParameter.SHEET_NAMES, sheetNames);
			}
			exporter.exportReport();
			res = 0;
		} else if (PDF_TYPE.equals(type)) {
			// jasperReport = JasperCompileManager.compileReport(rptXMLPath);
			// jasperPrint =
			// JasperFillManager.fillReport(jasperReport,paramMap,conn);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					exportPath);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);
			exporter.exportReport();
			res = 0;
		} else if (HTML_TYPE.equals(type)) {
			// jasperReport = JasperCompileManager.compileReport(rptXMLPath);
			// jasperPrint =
			// JasperFillManager.fillReport(jasperReport,paramMap,conn);
			JRHtmlExporter exporter = new JRHtmlExporter();
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					exportPath);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);
			exporter.exportReport();
			res = 0;
		} else {
			// jasperReport = JasperCompileManager.compileReport(rptXMLPath);
			// jasperPrint =
			// JasperFillManager.fillReport(jasperReport,paramMap,conn);
			JExcelApiExporter exporter = new JExcelApiExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					exportPath);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);
			exporter.exportReport();
			res = 0;
		}
		return res;
	}
	
	public int exportRptToHTML(String rptXMLPath, String exportPath,
			HashMap<String, String> paramMap) throws ServiceException{
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.CotExportRptService#exportRptToPDF(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public int exportRptToPDF(String rptXMLPath, String exportPath,
			HashMap<String, String> paramMap) throws ServiceException{
		int res = -1;
		try {
			res = this.exportRpt(rptXMLPath, exportPath, paramMap, conn,
					this.PDF_TYPE);
		} catch (JRException e) {
			logger.error("导出PDF发生JR异常", e);
			String msg = ExceptionStackTracePaser.paserStactTrace(e);
			throw new ServiceException(msg);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.CotExportRptService#exportRptToXLS(java.lang.String, java.lang.String, java.util.HashMap)
	 */
	public int exportRptToXLS(String rptXMLPath, String exportPath,
			HashMap<String, String> paramMap) throws ServiceException{
		int res = -1;
		try {
			res = this.exportRpt(rptXMLPath, exportPath, paramMap, conn,
					this.XLS_TYPE);
		} catch (JRException e) {
			logger.error("导出EXCEL发生JR异常", e);
			String msg = ExceptionStackTracePaser.paserStactTrace(e);
			throw new ServiceException(msg);
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see com.sail.cot.service.CotExportRptService#getJasperPrint(java.lang.String, java.util.HashMap)
	 */
	public JasperPrint getJasperPrint(String rptXMLPath, HashMap<String, Object> paramMap) throws JRException{
		conn = super.getSession().connection();

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		JasperDesign design = null;

		
		String path = ContextUtil.getLocalTomcatHome()+Constants.DEFAULT_UPLOAD_FILE;
		
		String classpath = ContextUtil.getLocalTomcatSubPath("reportfile/tmp");

		String filePath = null;
		boolean bHeader = true;
		String headflag = (String) paramMap.get("HEADER_PER_PAGE");
		if (headflag != null && !headflag.equals("barcode")) {
			String tmpPath = rptXMLPath;
			if (rptXMLPath.indexOf(".jasper") != -1) {
				tmpPath = rptXMLPath.replace("jasper", "jrxml");
			}
			// 报表默认是显示表头的
			bHeader = Boolean.parseBoolean(String.valueOf(paramMap
					.get("HEADER_PER_PAGE")));
			// 需要分页显示
			if (!bHeader) {
				design = JRXmlLoader.load(tmpPath);
				design.setIgnorePagination(true);
				// 生成jxml文件
				filePath = classpath + File.separator
						+ String.valueOf(System.currentTimeMillis()) + ".jrxml";
				JRXmlWriter.writeReport((JRReport) design, filePath, "UTF-8");
			}
		}
		if (filePath != null)
			rptXMLPath = filePath;
		// japser文件，默认分页显示
		if (rptXMLPath.indexOf(".jasper") != -1 && bHeader) {
			jasperReport = (JasperReport) JRLoader.loadObject(rptXMLPath);
		} else {
			jasperReport = JasperCompileManager.compileReport(rptXMLPath);
		}
		System.out.println("Path:" + path);
		paramMap.put("SUBREPORT_DIR", path);
		jasperPrint = JasperFillManager.fillReport(jasperReport, paramMap, conn);
		if (filePath != null) {
			// 删除该文件
			File file = new File(filePath);
			file.delete();
		}
		return jasperPrint;
	}

}
