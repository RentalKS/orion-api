//package com.orion.service;
//
//import edu.stanford.nlp.pipeline.CoreDocument;
//import edu.stanford.nlp.pipeline.CoreEntityMention;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Properties;
//
//@Service
//public class NLPService {
//    private final StanfordCoreNLP pipeline;
//
//    public NLPService(){
//        Properties props = new Properties();
//        props.setProperty("annotators","tokenize,ssplit,pos,lemma,ner");
//        this.pipeline = new StanfordCoreNLP(props);
//    }
//
//    public List<CoreEntityMention> extractEntities(String text){
//        CoreDocument document = new CoreDocument(text);
//        pipeline.annotate(document);
//        return document.entityMentions();
//    }
//}
