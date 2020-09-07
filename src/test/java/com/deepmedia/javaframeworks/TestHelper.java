package com.deepmedia.javaframeworks;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import com.deepmedia.javaframeworks.entities.GithubResponseItem;
import com.deepmedia.javaframeworks.entities.RepositoryLicense;
import com.deepmedia.javaframeworks.entities.RepositoryOwner;
import com.deepmedia.javaframeworks.entities.UserAuth;
import com.deepmedia.javaframeworks.gateways.JavaFrameworksGateway;

/**
 * 
 * @author Mindaugas Lucka
 *
 */
public class TestHelper {

	public static void mockGateway(JavaFrameworksGateway gateway, UserAuth auth) {
		List<GithubResponseItem> repoItems = assembleRepoItems();
		List<GithubResponseItem> starredRepos = new ArrayList<>();
		Mockito.when(gateway.retrieveSearchedRepoItems(auth)).thenReturn(repoItems);
		Mockito.when(gateway.retrieveUserStarredRepos(auth)).thenReturn(starredRepos);
		Mockito.when(gateway.retrieveNumberOfContributors(repoItems.get(0).getContrUrl(), auth)).thenReturn(7);
		Mockito.when(gateway.retrieveNumberOfContributors(repoItems.get(1).getContrUrl(), auth)).thenReturn(3);
		Mockito.when(gateway.retrieveNumberOfContributors(repoItems.get(2).getContrUrl(), auth)).thenReturn(5);
		
		Mockito.when(gateway.changeRepoStar("owner", repoItems.get(0).getName(), true, auth)).thenReturn(true);
	}
	
	private static List<GithubResponseItem> assembleRepoItems() {
		List<GithubResponseItem> repoItems = new ArrayList<>();
		
		GithubResponseItem item1 = new GithubResponseItem();
		item1.setRepoId(1);
		item1.setName("framework-1");
		item1.setOwner(new RepositoryOwner());
		item1.setLicense(new RepositoryLicense());
		item1.setStarCount(15);
		item1.setContrUrl("https://api.github.com/repos/framework-1/contributors");
		
		GithubResponseItem item2 = new GithubResponseItem();
		item2.setRepoId(2);
		item2.setName("framework-2");
		item2.setOwner(new RepositoryOwner());
		item2.setLicense(new RepositoryLicense());
		item2.setStarCount(20);
		item2.setContrUrl("https://api.github.com/repos/framework-2/contributors");
		
		GithubResponseItem item3 = new GithubResponseItem();
		item3.setRepoId(3);
		item3.setName("framework-3");
		item3.setOwner(new RepositoryOwner());
		item3.setLicense(new RepositoryLicense());
		item3.setStarCount(10);
		item3.setContrUrl("https://api.github.com/repos/framework-3/contributors");
		
		repoItems.add(item1);
		repoItems.add(item2);
		repoItems.add(item3);
		
		return repoItems;
	}
}
