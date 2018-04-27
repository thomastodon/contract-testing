package thomastodon.io.sourceapp;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@SpringBootTest(classes = SourceAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
@RunWith(SpringRunner.class)
public abstract class ContractTest {

    @Inject MessageVerifier verifier;

//    @Before
//    public void setup() {
//        verifier.receive("egg", 100, MILLISECONDS);
//    }

    public void nothing() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
