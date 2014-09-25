import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceProcessException;
import org.xml.sax.SAXException;

/**
 * CasConsumer 
 * @author yan
 *
 */
public class CasConsumer extends CasConsumer_ImplBase {
  public static final String PARAM_FILE= "OutputFile";
  int docNum;
  File out = null;
  BufferedWriter bw = null;

  @Override
  public void initialize() {

    docNum = 0;
    String fileOutput = (String) getConfigParameterValue(PARAM_FILE);
    try {
    // out = new File("src/main/resources/data/hw1-yanhe.out");
    File out= new File(fileOutput);
    bw = new BufferedWriter(new FileWriter(out));
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  
  @Override
  public void processCas(CAS aCAS) throws ResourceProcessException {
    // TODO Auto-generated method stub
    JCas jcas = null;
   
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      //throw new CollectionException(e);
    }
    
    // get the file's name of the input file from CAS
    FSIterator<Annotation> iter = jcas.getAnnotationIndex(Genetag.type).iterator();
    System.out.println("Consuming CAS");
    String geneID = "";
    String geneContent = "";
    int start = -1;
    int end = -1;
    
    while (iter.hasNext()){
      Genetag annotation = (Genetag) iter.next();
      //System.out.println(annotation.getContent());
      geneID = annotation.getID();
      geneContent = annotation.getContent();
      start = annotation.getBegin();
      //System.out.println(start);
      end = annotation.getEnd();
    
    try {
      writeInFile(geneID, geneContent, start, end);
    } catch (IOException e) {
      throw new ResourceProcessException(e);
    } catch (SAXException e) {
      throw new ResourceProcessException(e);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    }
       
  }
  
  public void writeInFile(String geneIdentifier, String geneName, int start, int end)
          throws Exception {
        bw.write(geneIdentifier + "|" + start + " " + end + "|" + geneName);
        bw.newLine();
        bw.flush();
        //System.out.println("shit");
      }
  
  @Override
  public void destroy() {

    try {
      if (bw != null) {
        bw.close();
        bw = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}