package com.stalin.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.kohsuke.github.GHRepositorySearchBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// These old twitter4j imports are likely for the v1.1 API
// We'll leave them for now but comment out the code that uses them.
import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@RestController
public class RepositoryDetailsController {

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String getRepos() throws IOException {
        // 
        // CRITICAL CHANGE 1: REMOVED hard-coded credentials.
        // We will pass in a GitHub token from our Jenkins pipeline
        // as an environment variable (e.g., GITHUB_TOKEN)
        //
        String githubToken = env.getProperty("GITHUB_TOKEN");
        if (githubToken == null) {
            // Good practice to fall back to anonymous if no token is provided
            GitHub github = GitHubBuilder.fromEnvironment().build();
            return "Greetings from Valaxy Technologies (via anonymous GitHub access)";
        }
        
        GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
        // Just a simple search to prove it works
        GHRepositorySearchBuilder builder = github.searchRepositories().q("springboot");
        return "Greetings from Valaxy Technologies (found " + builder.list().getTotalCount() + " Spring Boot repos)";
    }

    @GetMapping("/trends")
    public Map<String, String> getTwitterTrends(@RequestParam("placeid") String trendPlace, @RequestParam("count") String trendCount) {
        
        // 
        // CRITICAL CHANGE 2: Commenting out the broken Twitter API call.
        // The old Twitter v1.1 API is deprecated, and this code will
        // fail with authentication errors.
        //
        // To get this working, the code would need to be rewritten for
        // the new Twitter API v2, which is a much bigger change.
        //
        // For our CI/CD pipeline, we just want the app to BUILD and RUN.
        // We will return a simple message instead.
        //
        Map<String, String> trendDetails = new HashMap<String, String>();
        trendDetails.put("status", "The Twitter API endpoint is currently disabled.");
        trendDetails.put("reason", "This app needs to be migrated to the new Twitter API v2.");
        return trendDetails;

        /* --- OLD BROKEN CODE ---
        String consumerKey = env.getProperty("CONSUMER_KEY");
        String consumerSecret = env.getProperty("CONSUMER_SECRET");
        String accessToken = env.getProperty("ACCESS_TOKEN");
        String accessTokenSecret = env.getProperty("ACCESS_TOKEN_SECRET");
        System.out.println("consumerKey "+consumerKey+" consumerSecret "+consumerSecret+" accessToken "+accessToken+" accessTokenSecret "+accessTokenSecret);      
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        System.out.println("Twitter Factory "+tf);
        System.out.println("Code testing purpose ");
        Twitter twitter = tf.getInstance();
        System.out.println("Twitter object "+twitter);
        Map<String, String> trendDetails = new HashMap<String, String>();
        try {
            Trends trends = twitter.getPlaceTrends(Integer.parseInt(trendPlace));
            System.out.println("After API call");
            int count = 0;
            for (Trend trend : trends.getTrends()) {
                if (count < Integer.parseInt(trendCount)) {
                    trendDetails.put(trend.getName(), trend.getURL());
                    count++;
                }
            }
        } catch (TwitterException e) {
            trendDetails.put("test", "MyTweet");
            //trendDetails.put("Twitter Exception", e.getMessage());
            System.out.println("Twitter exception "+e.getMessage());

        }catch (Exception e) {
            trendDetails.put("test", "MyTweet");
            System.out.println("Exception "+e.getMessage());
        }
        return trendDetails;
        */
    }

}
