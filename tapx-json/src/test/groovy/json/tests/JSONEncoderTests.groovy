package json.tests

import com.howardlewisship.tapx.json.JSONEncoder
import com.howardlewisship.tapx.json.JSONModule
import org.apache.tapestry5.ioc.Registry
import org.apache.tapestry5.ioc.RegistryBuilder
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
    }
}
