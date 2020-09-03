package com.deepmedia.javaframeworks.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
	
	@Value( "${github.api.baseurl}" )
	private String baseUrl;
	
	@Value( "${github.api.search.frameworks}" )
	private String searchStr;
	
	@Value( "${github.username}" )
	private String username;
	
	private JavaFrameworksGateway gateway;
	
	public List<JavaFramework> retrieveJavaFrameworks(String metric) {

		gateway = new JavaFrameworksGateway();
		
		List<GithubResponseItem> frameworkRepos = 
				gateway.retrieveSearchedRepoItems(baseUrl, searchStr);
		List<JavaFramework> javaFrameworks = mapItems(frameworkRepos);
		sortJavaFrameworks(javaFrameworks, metric);
		
		return javaFrameworks;

	}

	private List<JavaFramework> mapItems(
			List<GithubResponseItem> frameworkRepos) {
		List<JavaFramework> javaFrameworks = new ArrayList<>();

		List<GithubResponseItem> starredRepos = 
				gateway.retrieveUserStarredRepos(baseUrl, username);
		
		for (GithubResponseItem item : frameworkRepos) {
			Integer numOfContr = gateway.retrieveNumberOfContributors(item.getContrUrl());
			
			Boolean isStarred = starredRepos.stream().filter(
					s -> s.getRepoId().equals(item.getRepoId())).
					collect(Collectors.toList()).size() > 0;
			
			JavaFramework javaFramework = collectFrameworkDetails(item, numOfContr, isStarred);
			
			javaFrameworks.add(javaFramework);
		}

		return javaFrameworks;
	}

	private JavaFramework collectFrameworkDetails(
			GithubResponseItem item, Integer numOfContr, Boolean isStarred) {
		JavaFramework javaFramework = new JavaFramework();
		
		javaFramework.setRepoId(item.getRepoId());
		javaFramework.setName(item.getName());
		javaFramework.setDescription(item.getDescription());
		javaFramework.setLicenceName(item.getLicense().getName());
		javaFramework.setStarCount(item.getStarCount());
		javaFramework.setApiUrl(item.getApiUrl());
		javaFramework.setHtmlUrl(item.getHtmlUrl());
		javaFramework.setNumOfContr(numOfContr);
		javaFramework.setIsUserStarRepo(isStarred);
		
		return javaFramework;
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
