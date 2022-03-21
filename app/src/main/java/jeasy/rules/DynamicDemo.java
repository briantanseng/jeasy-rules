package jeasy.rules;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import jdk.jfr.events.FileReadEvent;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.jexl.JexlRuleFactory;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.*;

public class DynamicDemo {

//    MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());
    JexlRuleFactory ruleFactory = new JexlRuleFactory(new JsonRuleDefinitionReader());

    private static final String RULES_PATH = "src/main/resources/rules.csv";
    private static final String REQUEST_PAYLOAD_PATH = "request-payload.json";

    public void runDemo() throws Exception {
        System.out.println();
        System.out.println("~~~ Dynamic Demo ~~~");


        Map<String, Object> output = new HashMap<>();

        Facts facts = new Facts();
        facts.put("input", getRequestPayload().get("data"));
        facts.put("output", output);

        // create a rule set
        Rules rules = getRules();

        //create a default rules engine and fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();

        rulesEngine.fire(rules, facts);

        System.out.println(output);
    }

    private Rules getRules() throws Exception {
        String rules = getRuleDefinitions();
        return ruleFactory.createRules(Utils.getReader(rules));
    }

    private String getRuleDefinitions() throws Exception {
        File file = new File(RULES_PATH);
        String absolutePath = file.getAbsolutePath();

        CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(absolutePath));
        Map<String, String> values;

        List<Map<String, Object>> rules = new ArrayList<Map<String, Object>>();

        while ((values = reader.readMap()) != null) {

            Map<String, Object> rule = new HashMap<>();
            Set<String> keys = values.keySet();

            for (String key: keys ) {
                if (key.equals("actions")) {
                    String actions = values.get(key);
                    rule.put(key, actions.split(";"));
                } else if (key.equals("priority")) {
                    rule.put(key, Integer.valueOf(values.get(key)));
                }  else {
                    rule.put(key, values.get(key));
                }
            }

            rules.add(rule);
        }

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(rules);

        System.out.println(json);
        return json;
    }

    private Map<String, Object> getRequestPayload() throws Exception {

        try (InputStream inputStream =Thread.currentThread().getContextClassLoader().getResourceAsStream(REQUEST_PAYLOAD_PATH)) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readValue(inputStream, JsonNode.class);

            Map<String, Object> result = mapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>(){});
            return  result;

//            return jsonNode;
//            return mapper.writeValueAsString(jsonNode);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }
}
