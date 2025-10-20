package com.orion.service.chatbot;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreEntityMention;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class NLPService {
    private static final Logger logger = Logger.getLogger(NLPService.class.getName());
    private StanfordCoreNLP pipeline;

    public NLPService() {
        logger.info("NLPService constructor called, but pipeline is not initialized immediately.");
    }

    private synchronized void initializePipeline() {
        if (pipeline == null) {
            logger.info("Initializing Stanford NLP pipeline...");
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
            this.pipeline = new StanfordCoreNLP(props);
            logger.info("Stanford NLP pipeline initialized successfully.");
        }
    }

    public List<CoreEntityMention> extractEntities(String text) {
        initializePipeline();
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        return document.entityMentions();
    }
}
