package com.deepmedia.javaframeworks.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.deepmedia.javaframeworks.entities.GithubResponseItem;
import com.deepmedia.javaframeworks.entities.JavaFramework;
import com.deepmedia.javaframeworks.entities.KeyMetrics;
import com.deepmedia.javaframeworks.gateways.JavaFrameworksGateway;

/**
 * 
 * @author Mindaugas Lucka
 * 
 * Service for retrieving java frameworks from GitHub
 *
 */
@Service
public class JavaFrameworksService {
	
	@Value( "${github.url}" )
	private String url;
	
	@Value( "${github.username}" )
	private String username;
	
	private JavaFrameworksGateway gateway;
	
	public List<JavaFramework> retrieveJavaFrameworks(String metric) {

		gateway = new JavaFrameworksGateway();
		
		List<GithubResponseItem> itemList = gateway.retrieveRepoItems(url);
		List<JavaFramework> javaFrameworks = mapItems(itemList);
		sortJavaFrameworks(javaFrameworks, metric);
		
		return javaFrameworks;

	}

	private List<JavaFramework> mapItems(List<GithubResponseItem> itemList) {
		List<JavaFramework> javaFrameworks = new ArrayList<>();

		for (GithubResponseItem item : itemList) {
			JavaFramework javaFramework = new JavaFramework();
			javaFramework.setName(item.getName());
			javaFramework.setDescription(item.getDescription());
			javaFramework.setLicenceName(item.getLicense().getName());
			javaFramework.setStarCount(item.getStarCount());
			javaFramework.setApiUrl(item.getApiUrl());
			javaFramework.setHtmlUrl(item.getHtmlUrl());
			javaFramework.setNumOfContr(
					gateway.retrieveNumberOfContr(item.getContrUrl()));
			
			javaFrameworks.add(javaFramework);
		}

		return javaFrameworks;
	}

	
	private void sortJavaFrameworks(List<JavaFramework> javaFrameworks, String metric) {
		
		Comparator<JavaFramework> comparatorByNumOfContr = (JavaFramework jf1, JavaFramework jf2) -> 
        jf1.getNumOfContr().compareTo( jf2.getNumOfContr());
		
        Comparator<JavaFramework> comparatorByStarCount = (JavaFramework jf1, JavaFramework jf2) -> 
        jf1.getStarCount().compareTo( jf2.getStarCount());
        
		if(KeyMetrics.NUM_OF_CONTR.getName().equals(metric.toLowerCase())) {
			Collections.sort(javaFrameworks, comparatorByNumOfContr);
		} else if(KeyMetrics.STAR_COUNT.getName().equals(metric.toLowerCase())) {
			Collections.sort(javaFrameworks, comparatorByStarCount);
		} else {
			Collections.sort(javaFrameworks, comparatorByStarCount);
		}
		
	}
	

}
