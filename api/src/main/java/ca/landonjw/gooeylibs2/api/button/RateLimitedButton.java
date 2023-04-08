package ca.landonjw.gooeylibs2.api.button;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitedButton extends ButtonBase {
    private final Button button;
    private final int limit;
    private final long timeInterval;
    private final TimeUnit timeUnit;
    private final LinkedList<Instant> lastActionTimes = new LinkedList<>();

    protected RateLimitedButton(@Nonnull Button button, int limit, long timeInterval, @Nonnull TimeUnit timeUnit) {
        super(button.getDisplay());
        this.button = button;
        this.limit = limit;
        this.timeInterval = timeInterval;
        this.timeUnit = timeUnit;
    }

    @Override
    public void onClick(@Nonnull ButtonAction action) {
        if (this.isRateLimited()) {
            return;
        }
        if (this.isTopElementAboveTimeThreshold()) {
            this.lastActionTimes.remove();
        }
        this.button.onClick(action);
        this.lastActionTimes.add(Instant.now());
    }

    public boolean isRateLimited() {
        if (this.lastActionTimes.size() < this.limit) {
            return false;
        }
        return !this.isTopElementAboveTimeThreshold();
    }

    public int getAllowedRemainingClicks() {
        AtomicInteger allowed = new AtomicInteger(this.limit);
        this.lastActionTimes.forEach(instant -> {
            if (Duration.between(instant, Instant.now()).toMillis() <= this.timeUnit.toMillis(this.timeInterval)) {
                allowed.getAndDecrement();
            }
        });
        return allowed.get();
    }

    private boolean isTopElementAboveTimeThreshold() {
        if (this.lastActionTimes.isEmpty()) {
            return false;
        }
        Instant earliestActionTime = this.lastActionTimes.peek();
        return Duration.between(earliestActionTime, Instant.now()).toMillis() > this.timeUnit.toMillis(this.timeInterval);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Button button;
        private int limit;
        private long timeInterval;
        private TimeUnit timeUnit;

        public Builder button(Button button) {
            this.button = button;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder interval(long time, @Nonnull TimeUnit timeUnit) {
            this.timeInterval = time;
            this.timeUnit = timeUnit;
            return this;
        }

        public RateLimitedButton build() {
            return new RateLimitedButton(this.button, this.limit, this.timeInterval, this.timeUnit);
        }
    }
}

