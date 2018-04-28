package thomastodon.io.sinkapp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EggController {

    @GetMapping("/eggs/count")
    public Map<String, Integer> howManyEggsAreThere() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("count", 4);
        return map;
    }
}
