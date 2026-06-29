package com.aisupport.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDTO {

    private Long totalChats;

    private Long activeChats;

    private Long totalLeads;

    private Long newLeads;

    private Long contactedLeads;

    private Long qualifiedLeads;

    private Long closedLeads;

    private Double conversionRate;
}