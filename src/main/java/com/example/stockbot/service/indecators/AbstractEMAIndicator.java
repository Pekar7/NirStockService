package com.example.stockbot.service.indecators;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.RecursiveCachedIndicator;
import org.ta4j.core.num.Num;

public abstract class AbstractEMAIndicator extends RecursiveCachedIndicator<Num> {

    private final Indicator<Num> indicator;
    private final int barCount;
    private final Num multiplier;

    protected AbstractEMAIndicator(Indicator<Num> indicator, int barCount, double multiplier) {
        super(indicator);
        this.indicator = indicator;
        this.barCount = barCount;
        this.multiplier = numOf(multiplier);
    }

    @Override
    protected Num calculate(int index) {
        if (index == 0) {
            return indicator.getValue(0);
        }
        Num prevValue = getValue(index - 1);
        return indicator.getValue(index).minus(prevValue).multipliedBy(multiplier).plus(prevValue);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " barCount: " + barCount;
    }

    public int getBarCount() {
        return barCount;
    }
}

