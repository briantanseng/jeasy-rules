package jeasy.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;

/**
 * MVEL Demo
 * Refer to <a href="https://en.wikibooks.org/wiki/Transwiki:MVEL_Language_Guide">MVEL Language Guide</a>
 */
public class MvelDemo {

    MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());

    public void runDemo() throws Exception {
        System.out.println();
        System.out.println("~~~ MVEL demo ~~~");

        //create a person instance (fact)
        Shop shop = new Shop();
        Person tom = new Person("Tom", 20);

        System.out.println("Tom's age is "+tom.getAge());

        Facts facts = new Facts();
        facts.put("person", tom);
        facts.put("shop", shop);

        // create a rule set
        Rules rules = new Rules();
        rules.register(getAgeRule());
        rules.register(getAlcoholRule());

        //create a default rules engine and fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();

        System.out.println("Tom: Hi! can I have some Vodka please?");
        rulesEngine.fire(rules, facts);

        System.out.println("Result: "+tom.getAlcoholEligibility());
    }

    private Rule getAgeRule() throws Exception {
        String rule = getAgeRuleDefinition();
        System.out.println("\n"+rule);
        return ruleFactory.createRule(Utils.getReader(rule));
    }

    private String getAgeRuleDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: \"age rule\"").append("\n");
        sb.append("description: \"Check if person's age is > 18 and mark the person as adult\"").append("\n");
        sb.append("priority: 1").append("\n");
        sb.append("condition: \"person.age >= 18\"").append("\n");
        sb.append("actions:").append("\n");
        sb.append("  - \"person.setAdult(true);\"").append("\n");
        sb.append("  - \"person.setAlcoholEligibility(\\\"Rock en roll! \\\" + person.getName() + \\\" can buy alcohol.\\\");\"").append("\n");
        sb.append("  - \"shop.acceptPurchaseMessage(person.getName())\"").append("\n");
        return sb.toString();
    }

    private Rule getAlcoholRule() throws Exception {
        String rule = getAlcoholRuleDefinition();
        System.out.println("\n"+rule);
        return ruleFactory.createRule(Utils.getReader(rule));
    }

    private String getAlcoholRuleDefinition() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: \"alcohol rule\"").append("\n");
        sb.append("description: \"children are not allowed to buy alcohol\"").append("\n");
        sb.append("priority: 2").append("\n");
        sb.append("condition: \"person.isAdult() == false\"").append("\n");
        sb.append("actions:").append("\n");
        sb.append("  - \"shop.declinePurchaseMessage(person.getName())\"").append("\n");
        sb.append("  - \"person.setAlcoholEligibility(person.getName() + \\\" is not allowed to buy alcohol.\\\");\"").append("\n");
        return sb.toString();
    }
}
