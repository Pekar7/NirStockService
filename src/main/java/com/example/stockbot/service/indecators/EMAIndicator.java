package com.example.stockbot.service.indecators;

import org.ta4j.core.Indicator;
import org.ta4j.core.indicators.AbstractEMAIndicator;
import org.ta4j.core.num.Num;

public class EMAIndicator extends AbstractEMAIndicator {

    public EMAIndicator(Indicator<Num> indicator, int barCount) {
        super(indicator, barCount, (2.0 / (barCount + 1)));
    }
}
