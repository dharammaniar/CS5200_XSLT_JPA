package edu.neu.cs5200.dao;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import edu.neu.cs5200.models.Site;
import edu.neu.cs5200.models.SiteList;

public class SiteDao {
	
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("XSLT_JPA");
	EntityManager em = factory.createEntityManager();
	
	public Site findSite(int siteId) {
		return em.find(Site.class, siteId);
	}
	
	@SuppressWarnings("unchecked")
	public List<Site> findAllSites() {
		Query query = em.createQuery("select site from Site site");
		return (List<Site>)query.getResultList();
	}
	
	public void exportSiteDatabaseToXmlFile(SiteList siteList, String xmlFileName) {
		File xmlFile = new File(xmlFileName);
		try {
			JAXBContext jaxb = JAXBContext.newInstance(SiteList.class);
			Marshaller marshaller = jaxb.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(siteList, xmlFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void convertXmlFileToOutputFile(
			String inputXmlFileName,
			String outputFileName,
			String xsltFileName)
	{
		File inputXmlFile = new File(inputXmlFileName);
		File outputXmlFile = new File(outputFileName);
		File xsltFile = new File(xsltFileName);
		
		StreamSource source = new StreamSource(inputXmlFile);
		StreamSource xslt    = new StreamSource(xsltFile);
		StreamResult output = new StreamResult(outputXmlFile);
		
		TransformerFactory factory = TransformerFactory.newInstance();
		try {
			Transformer transformer = factory.newTransformer(xslt);
			transformer.transform(source, output);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SiteDao dao = new SiteDao();
		
		Site site = dao.findSite(1);
		System.out.println(site.getName());
		
		List<Site> siteList = dao.findAllSites();
		for(Site site1:siteList) {
			System.out.println(site1.getName());
		}
		
		SiteList theSiteList = new SiteList();
		theSiteList.setSites(siteList);
		
		dao.exportSiteDatabaseToXmlFile(theSiteList, "xml/sites.xml");
		
		dao.convertXmlFileToOutputFile("xml/sites.xml", "xml/sites.html", "xml/sites2html.xslt");
		dao.convertXmlFileToOutputFile("xml/sites.xml", "xml/equipment.html", "xml/sites2equipment.xslt");
	}

}
