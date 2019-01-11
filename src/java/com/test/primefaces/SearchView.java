package com.test.primefaces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import com.predic8.schema.ComplexContent;
import com.predic8.schema.ComplexType;
import com.predic8.schema.Element;
import com.predic8.schema.Schema;
import com.predic8.schema.Sequence;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.Operation;
import com.predic8.wsdl.PortType;
import com.predic8.wsdl.WSDLParser;
import java.util.HashMap;

/**
 *
 * @author dell5430
 */
@ManagedBean
public class SearchView {

    private String text1;
    private String text2;
    static boolean emptyFlag = true;
    static boolean meettedFlag = false;
    static String currentStep = "";
    static boolean checkContextFlag = false;

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public void searchField() throws IOException {
        String project = "ESB_Get_BillingProfileInfo";
        String fieldNameForSearch = text1;
        File folder = new File(testSearch.class.getResource("/FileWsdl/" + project).getPath());
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            System.out.println(file.getName());
            Path path = Paths.get(file.getPath());

            System.out.println("aaaaaaaaa" + path.toString());
            WSDLParser parser = new WSDLParser();
            Definitions defs = parser.parse(path.toString());
            HashMap<String, ComplexType> allComplexType = new HashMap<>();

            // Get All ComplexType To HashMap On Current File
            for (Schema schema : defs.getSchemas()) {
                if (!schema.getComplexTypes().isEmpty()) {
                    for (ComplexType complexType : schema.getComplexTypes()) {
                        allComplexType.put(complexType.getName(), complexType);
                    }
                } else if (!schema.getElements().isEmpty()) {
                    for (Element element : schema.getElements()) {
                        ComplexType cpt = (ComplexType) element.getEmbeddedType();
                        allComplexType.put(element.getName(), cpt);
                    }

                }

            }

            checkContextType(defs, allComplexType, fieldNameForSearch);

        }
        if (emptyFlag) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ไม่พบ Field/Method ที่ท่านระบุ"));
        }
        emptyFlag = true;
    }

    private void checkContextType(Definitions defs, HashMap<String, ComplexType> allComplexType, String fieldNameForSearch) {
        String method = "";
        String responseName = "";

        for (PortType portType : defs.getPortTypes()) {
            for (Operation operation : portType.getOperations()) {
                method = operation.getName();
                responseName = operation.getOutput().getMessage().getName();
                currentStep = responseName;
                stepCheckContextType(allComplexType, fieldNameForSearch, operation, portType.getName());
            }
        }
    }

    private void stepCheckContextType(HashMap<String, ComplexType> allComplexType, String fieldNameForSearch, Operation operation, String fileName) {
        System.out.println(currentStep);
        if (allComplexType.get(currentStep).getSequence() != null) {
            for (Element element : allComplexType.get(currentStep).getSequence().getElements()) {
                if (element.getName().equals(fieldNameForSearch)) {
                    System.out.println("'" + fieldNameForSearch + "'" + " is in ....");
                    System.out.println("File Name : " + fileName);
                    System.out.println("Method : " + operation.getName() + "\n");
                    if (fileName.contains("PortType")) {
                        fileName = fileName.substring(0, (fileName.length() - 8));
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Name : " + fileName + " === Method : " + operation.getName() + "\n"));
                    emptyFlag = false;
                    meettedFlag = true;
                    break;
                } else if (!element.getType().toString().contains("}string")
                        && !element.getType().toString().contains("}int")
                        && !element.getType().toString().contains("}double")
                        && !element.getType().toString().contains("}long")
                        && !element.getType().toString().contains("}short")
                        && !element.getType().toString().contains("}float")
                        && !element.getType().toString().contains("}base64Binary")
                        && !element.getType().toString().contains("}date")
                        && !element.getType().toString().contains("}decimal")
                        && !element.getType().toString().contains("}dateTime")) {
                    String[] splitType = element.getType().toString().split("}");
                    currentStep = splitType[1];

                    checkContextFlag = true;
                    recheckStepCheckContextType(allComplexType, fieldNameForSearch, operation, fileName);
                }
                if (meettedFlag) {
                    meettedFlag = false;
                    break;
                }
            }
        } else if (allComplexType.get(currentStep).getModel() != null) {
            ComplexContent complexContent = (ComplexContent) allComplexType.get(currentStep).getModel();
            Sequence sequence = (Sequence) complexContent.getDerivation().getModel();

            for (Element element : sequence.getElements()) {
                if (element.getName().equals(fieldNameForSearch)) {
                    System.out.println("'" + fieldNameForSearch + "'" + " is in ....");
                    System.out.println("File Name : " + fileName);
                    System.out.println("Method : " + operation.getName() + "\n");
                    if (fileName.contains("PortType")) {
                        fileName = fileName.substring(0, (fileName.length() - 8));
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("File Name : " + fileName + " === Method : " + operation.getName() + "\n"));
                    emptyFlag = false;
                    meettedFlag = true;
                    break;
                } else if (!element.getType().toString().contains("}string")
                        && !element.getType().toString().contains("}int")
                        && !element.getType().toString().contains("}double")
                        && !element.getType().toString().contains("}long")
                        && !element.getType().toString().contains("}short")
                        && !element.getType().toString().contains("}base64Binary")
                        && !element.getType().toString().contains("}date")
                        && !element.getType().toString().contains("}float")
                        && !element.getType().toString().contains("}decimal")
                        && !element.getType().toString().contains("}dateTime")) {
                    String[] splitType = element.getType().toString().split("}");
                    currentStep = splitType[1];

                    checkContextFlag = true;
                    recheckStepCheckContextType(allComplexType, fieldNameForSearch, operation, fileName);
                }
                if (meettedFlag) {
                    meettedFlag = false;
                    break;
                }
            }

        }
    }

    private void recheckStepCheckContextType(HashMap<String, ComplexType> allComplexType, String fieldNameForSearch, Operation operation, String fileName) {
        if (checkContextFlag) {
            stepCheckContextType(allComplexType, fieldNameForSearch, operation, fileName);

            checkContextFlag = false;
        }
    }

}
