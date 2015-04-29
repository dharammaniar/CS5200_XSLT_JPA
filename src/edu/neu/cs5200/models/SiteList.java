package edu.neu.cs5200.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(value = XmlAccessType.FIELD)
public class SiteList {
	@XmlElement(name="director")
	private List<Site> sites;

	public SiteList() {
		super();
	}

	public SiteList(List<Site> sites) {
		super();
		this.sites = sites;
	}
	
	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

}

