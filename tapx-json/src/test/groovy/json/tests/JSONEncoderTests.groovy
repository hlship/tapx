package json.tests

import com.howardlewisship.tapx.json.JSONEncoder
import com.howardlewisship.tapx.json.JSONModule
import org.apache.tapestry5.ioc.Registry
import org.apache.tapestry5.ioc.RegistryBuilder
import org.apache.tapestry5.json.JSONArray
import org.apache.tapestry5.json.JSONObject
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Tests various implementations of {@link com.howardlewisship.tapx.json.JSONEncoder}.
 */
class JSONEncoderTests extends Specification {

    @Shared
    Registry registry = new RegistryBuilder().add(JSONModule.class).build();

    @Shared
    JSONEncoder encoder = registry.getService(JSONEncoder.class)

    @Unroll("JSON encoding of #input should be #expected")
    def "encoding of simple value types"() {
        expect:
        encoder.encodeAsJSON(input) == expected

        where:

        input      | expected

        "a string" | "a string"
        5          | 5
        false      | false
        true       | true
        3.75f      | 3.75f
        null       | null
    }

    def "map encodes to JSONObject"() {
        when:
        def input = [key1: "value1", key2: 2, key3: true]
        def output = encoder.encodeAsJSON(input)
        def expected = new JSONObject("{ 'key1' : 'value1', 'key2' : 2, 'key3' : true }")

        then:

        output == expected
    }

    def "list encodes to JSONArray"() {
        when:

        def input = [1, 2, 3]
        def output = encoder.encodeAsJSON(input)
        def expected = new JSONArray(1, 2, 3)

        then:

        output == expected
    }

    def "encoding of nested maps and lists"() {
        when:

        def input = [top: [level: 'nested'], anArray: [1, 2, 3, [level: 'deep']]]
        def output = encoder.encodeAsJSON(input)
        def expected = new JSONObject("""
              { "top" : { "level" : "nested" },
                 "anArray" : [1, 2, 3, { "level" : "deep" }]}
         """)

        then:

        output == expected
    }


}
