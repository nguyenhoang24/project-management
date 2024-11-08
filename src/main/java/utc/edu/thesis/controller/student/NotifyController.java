package utc.edu.thesis.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.util.RedisUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
public class NotifyController {
    private final RedisUtil<Integer> integerRedisUtil;

    @PostMapping("/get-notify/{id}")
    public ResponseEntity<Integer> getNotifyFromCache(@PathVariable Long id) {
        if(integerRedisUtil.getValue(String.valueOf(id)) != null) {
            Integer cacheValue = integerRedisUtil.getValue(String.valueOf(id));
            integerRedisUtil.deleteValue(String.valueOf(id));
            return ResponseEntity.ok(cacheValue);
        }

        return ResponseEntity.ok(0);
    }

    @PostMapping("/get-notify-day/{id}")
    public ResponseEntity<Integer> getNotifyDayFromCache(@PathVariable Long id) {
        if(integerRedisUtil.getValue(id+"-day") != null) {
            Integer cacheValue = integerRedisUtil.getValue(id+"-day");
            integerRedisUtil.deleteValue(id+"-day");
            return ResponseEntity.ok(cacheValue);
        }

        return ResponseEntity.ok(0);
    }

    @PostMapping("/get-notify-hour/{id}")
    public ResponseEntity<Integer> getNotifyHourFromCache(@PathVariable Long id) {
        if(integerRedisUtil.getValue(id+"-hour") != null) {
            Integer cacheValue = integerRedisUtil.getValue(id+"-hour");
            integerRedisUtil.deleteValue(id+"-hour");
            return ResponseEntity.ok(cacheValue);
        }

        return ResponseEntity.ok(0);
    }
}
