package tg.ngstars.interv.dto;

import java.math.BigDecimal;

public record UpdateBillingRequest(
    boolean billable,
    BigDecimal billingAmount,
    String billingNotes
) {}
