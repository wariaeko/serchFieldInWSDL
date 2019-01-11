package com.test.primefaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class testSearch {

    public static void main(String[] args)
            throws FileNotFoundException, SAXException, IOException, ParserConfigurationException {
        String project = "ESB_Get_BillingProfileInfo";
        File folder = new File(testSearch.class.getResource("/FileWsdl/" + project).getPath());
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
//            System.out.println(file.getName());
            Path path = Paths.get(file.getPath());
            List<String> allWordInWsdl = Files.lines(path).collect(Collectors.toList());
            System.out.println(allWordInWsdl);
//            showFileName("subscriber", allWordInWsdl, file);
        }
    }

    public static String getFieldName() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Field's Name : ");
        String fieldName = sc.nextLine();
        sc.close();

        return fieldName;
    }

    public static void showFileName(String searchField, List<String> allWordInWsdl, File file) {
        boolean resultSearch = false;
        for (String str : allWordInWsdl) {
            if (str.toLowerCase().contains(searchField.toLowerCase())) {
                resultSearch = true;
            }
        }
        if (resultSearch) {
            System.out.println(file.getName());
        } else {
            System.out.println("\nNo, " + searchField + " is Not This File!" + "\n");
        } //spendingLimitInfo
    }
}
