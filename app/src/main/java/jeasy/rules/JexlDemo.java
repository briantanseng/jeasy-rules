package jeasy.rules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.jexl.JexlRuleFactory;
import org.jeasy.rules.support.reader.JsonRuleDefinitionReader;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;

/**
 * JEXL Demo
 * @see
 * <ul>
 *     <li><a href="https://commons.apache.org/proper/commons-jexl/reference/syntax.html" target="_top">JEXL Syntax</a></li>
 *     <li><a href="http://commons.apache.org/proper/commons-jexl/reference/examples.html" target="_top">JEXL Examples</a></li>
 * </ul>
 */
public class JexlDemo {

    JexlRuleFactory ruleFactory = new JexlRuleFactory(new JsonRuleDefinitionReader());

    public void runDemo() throws Exception {
        System.out.println();
        System.out.println("~~~ JEXL demo ~~~");

        //create a person instance (fact)
        Shop shop = new Shop();
        Person tom = new Person("Tom", 20);

        System.out.println("Tom's age is "+tom.getAge());

        Facts facts = new Facts();
        facts.put("person", tom);
        facts.put("shop", shop);

        // create a rule set
        Rules rules = getRules();

        //create a default rules engine and fire rules on known facts
        RulesEngine rulesEngine = new DefaultRulesEngine();

        System.out.println("Tom: Hi! can I have some Vodka please?");
        rulesEngine.fire(rules, facts);

        System.out.println("Result: "+tom.getAlcoholEligibility());
    }

    private Rules getRules() throws Exception {
        String rules = getRuleDefinitions();
        System.out.println("\n"+rules);
        return ruleFactory.createRules(Utils.getReader(rules));
    }

    private String getRuleDefinitions() {
        final String ruleDefinition =
                "[" + "\n" +
                "  {" + "\n" +
                "    \"name\": \"age rule\"," + "\n" +
                "    \"description\": \"Check if person's age is >= 18 and mark the person as adult\"," + "\n" +
                "    \"priority\": 1," + "\n" +
                "    \"condition\": \"person.age >= 18\"," + "\n" +
                "    \"actions\": [" + "\n" +
                "      \"person.setAdult(true);\"," + "\n" +
                "      \"person.setAlcoholEligibility(\\\"Rock en roll! \\\" + person.getName() + \\\" can buy alcohol.\\\");\"," + "\n" +
                "      \"shop.acceptPurchaseMessage(person.getName());\"" + "\n" +
                "    ]" + "\n" +
                "  }," + "\n" +
                "  {" + "\n" +
                "    \"name\": \"alcohol rule\"," + "\n" +
                "    \"description\": \"children are not allowed to buy alcohol\"," + "\n" +
                "    \"priority\": 2," + "\n" +
                "    \"condition\": \"person.isAdult() == false\"," + "\n" +
                "    \"actions\": [" + "\n" +
                "      \"shop.declinePurchaseMessage(person.getName());\"," + "\n" +
                "      \"person.setAlcoholEligibility(person.getName() + \\\" is not allowed to buy alcohol.\\\");\"" + "\n" +
                "    ]" + "\n" +
                "  }" + "\n" +
                "]";
        return ruleDefinition;
    }


}
