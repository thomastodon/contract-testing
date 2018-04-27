package thomastodon.io.sourceapp;

import org.springframework.amqp.core.Exchange;

import java.util.Collection;
import java.util.Map;

public class EggExchange implements Exchange {
    @Override
    public String getName() {
        return "egg";
    }

    @Override
    public String getType() {
        return "direct";
    }

    @Override
    public boolean isDurable() {
        return true;
    }

    @Override
    public boolean isAutoDelete() {
        return false;
    }

    @Override
    public Map<String, Object> getArguments() {
        return null;
    }

    @Override
    public boolean isDelayed() {
        return false;
    }

    @Override
    public boolean isInternal() {
        return false;
    }

    @Override
    public boolean shouldDeclare() {
        return true;
    }

    @Override
    public Collection<?> getDeclaringAdmins() {
        return null;
    }

    @Override
    public boolean isIgnoreDeclarationExceptions() {
        return true;
    }
}
