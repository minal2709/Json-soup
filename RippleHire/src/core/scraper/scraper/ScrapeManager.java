package core.scraper.scraper;

import com.google.gson.Gson;

import core.scraper.model.JobPost;
import core.scraper.model.JobPostFields;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrapeManager {

	private static final String SEARCH_RESULTS = "https://sjobs.brassring.com/TGWebHost/searchresults.aspx";
	private static final String ID = "id";
	private static final String TG_CONTENT = "tg_content";

	public static void main(String[] args) {
		try {
			String strPageURL = "https://sjobs.brassring.com/TGWebHost/home.aspx?partnerid=25667&siteid=5417";
			srapeAndPrintResult(strPageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void srapeAndPrintResult(String strPageURL)
			throws FileNotFoundException {
		try {
			System.out.println("Getting cookies");
			Map<String, String> cookies = getCookies(strPageURL);
			System.out.println("Getting job posts");
			Document doc = getDoc(SEARCH_RESULTS, cookies);
			if (doc != null) {
				printJobs(doc, cookies);
			} else {
				System.out.println("Null Html found for search results");
			}
		} catch (IOException e) {
			System.out.println("Could not get the jobs");
		}

	}

	private static void printJobs(Document doc, Map<String, String> cookies) {
		Set<String> jobUrls = getJobUrls(doc.toString());
		if (jobUrls != null && !jobUrls.isEmpty()) {
			Gson gson = new Gson();
			int count = 0;
			for (String url : jobUrls) {
				try {
					Document d = getDoc(url, cookies);
					Element elems = d.getElementsByAttributeValue(ID,
							TG_CONTENT).first();
					JobPost post = getJobPost(url, elems);
					if (post != null) {
						count++;
						System.out.println(gson.toJson(post));
					}
				} catch (Exception e) {
					System.out.println("Error getting post for url "
							+ url);
				}
			}
			System.out.println("Got " + count
					+ " successful posts out of " + jobUrls.size());
		} else {
			System.out.println("No Job posts found");
		}
	}

	private static JobPost getJobPost(String url, Element elems) {
		JobPost post = null;
		if (elems != null) {
			post = new JobPost(url);
			post.setDesignation(getStringValue(elems
					.getElementsByAttributeValue(ID, JobPostFields.DESIGNATION)));
			post.setLocation(getStringValue(elems.getElementsByAttributeValue(
					ID, JobPostFields.LOCATION)));
			post.setJobFamily(getStringValue(elems.getElementsByAttributeValue(
					ID, JobPostFields.JOB_FAMILY)));
			post.setJobDescription(getStringValue(elems
					.getElementsByAttributeValue(ID,
							JobPostFields.JOB_DESCRIPTION)));
			post.setExperiance(getStringValue(elems
					.getElementsByAttributeValue(ID, JobPostFields.EXPERIENCE)));
			post.setQualification(getStringValue(elems
					.getElementsByAttributeValue(ID,
							JobPostFields.QUALIFICATION)));
			post.setNoOfPositions(getIntValue(elems
					.getElementsByAttributeValue(ID, JobPostFields.NO_POSITIONS)));
			post.setSkill(getStringValue(elems.getElementsByAttributeValue(ID,
					JobPostFields.SKILL)));
			post.setRemovalDate(getDateValue(elems.getElementsByAttributeValue(
					ID, JobPostFields.REMOVAL_DATE)));
		}
		return post;
	}

	private static Set<String> getJobUrls(String doc) {
		Set<String> jobUrls = new HashSet<String>();
		Pattern p = Pattern.compile("jobId=(\\d+)");
		Matcher m = p.matcher(doc);
		String pre = "https://sjobs.brassring.com/TGWebHost/jobdetails.aspx?jobId=";
		while (m.find()) {
			StringBuilder sb = new StringBuilder(pre);
			sb.append(m.group(1));
			jobUrls.add(sb.toString());
		}
		return jobUrls;
	}

	private static Document getDoc(String url, Map<String, String> cookies)
			throws IOException {
		return Jsoup.connect(url).cookies(cookies).get();
	}

	private static Map<String, String> getCookies(String strPageURL)
			throws IOException {
		return Jsoup.connect(strPageURL).execute().cookies();
	}

	private static Date getDateValue(Elements elements) {
		Date toRet = null;
		if (elements != null && elements.text() != null) {
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			try {
				toRet = format.parse(elements.text());
			} catch (ParseException e) {
				System.out.println("ERROR : Not a date : " + elements.text());
			}
		}
		return toRet;
	}

	private static Integer getIntValue(Elements elements) {
		Integer toRet = null;
		if (elements != null) {
			try {
				toRet = Integer.parseInt(elements.text());
			} catch (NumberFormatException e) {
				System.out.println("ERROR : Not a number : " + elements.text());
			}
		}
		return toRet;
	}

	private static String getStringValue(Elements elements) {
		String toRet = null;
		if (elements != null) {
			toRet = elements.text();
		}
		return toRet;
	}
}
