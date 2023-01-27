package com.testtask;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
public class Controller {
    @GetMapping("/companies")
    List<String> companies() throws IOException {
        Resource companies = new ClassPathResource("dict.txt");
        File file = companies.getFile();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Stream<String> linesCompanies = bufferedReader.lines();
        List<String> listCompanies = linesCompanies.toList();
        List<String> textLines = openFile();
        List<String> results = new ArrayList<>();
        int position = 0;
        for (String company: listCompanies) {
            for (String line: textLines) {
                ++position;
                if (line.toUpperCase().contains(company)) {
                    results.add("Company name: " + company + "; Position: " + position
                            + "; Length: " + company.length());
                }
            }
        }
        return results;
    }

    @GetMapping("/datesAndSums")
    List<String> datesAndSums() throws IOException {
        Pattern regexDate = Pattern.compile("([A-Z]{1}[A-Za-z]{2,8} [0-9]{1}[,0-9]{1}([,]{0,1})? [0-9]{4})");
        Pattern regexSum = Pattern.compile("((?!0.*$)(\\$[0-9]{1,3}(,[0-9]{3})?(,[0-9]{3})?(\\.[0-9]{2,3})?))");
        List<String> lines = openFile();
        List<String> results = new ArrayList<>();
        int position = 0;
        for (String line: lines) {
            ++position;
            Matcher matcherDate = regexDate.matcher(line);
            if (matcherDate.find()) {
                results.add("Date: " + matcherDate.group(1) + "; Position: " + position
                        + "; Length: " + (matcherDate.end() - matcherDate.start()));
            }
            Matcher matcherSum = regexSum.matcher(line);
            if (matcherSum.find()) {
                results.add("Sum: " + matcherSum.group(1) + ": Position: " + position
                        + "; Length: " + (matcherSum.end() - matcherSum.start()));
            }
        }
        return results;
    }

    @GetMapping("/sentences")
    List<String> sentences() throws IOException {
        Pattern re = Pattern.compile("[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
                Pattern.MULTILINE | Pattern.COMMENTS);
        List<String> lines = openFile();
        String text = lines.toString();
        List<String> results = new ArrayList<>();
        Matcher matcher = re.matcher(text);
        while (matcher.find()) {
            results.add("Sentence: " + matcher.group() + "; Length: " + (matcher.end() - matcher.start()));
        }
        return results;
    }

    private List<String> openFile() throws IOException {
        Resource resource = new ClassPathResource("test1.txt");
        File file = resource.getFile();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Stream<String> stream = bufferedReader.lines();
        List<String> lines = stream.toList();
        return lines;
    }
}