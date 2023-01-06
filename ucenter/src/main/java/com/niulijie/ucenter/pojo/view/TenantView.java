package com.niulijie.ucenter.pojo.view;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TenantView {

  @JsonProperty("tenant_id")
  private Integer tenantId;

  @JsonProperty("tenant_name")
  private String tenantName;

  public Integer getTenantId() {
    return tenantId;
  }

  public void setTenantId(Integer tenantId) {
    this.tenantId = tenantId;
  }

  public String getTenantName() {
    return tenantName;
  }

  public void setTenantName(String tenantName) {
    this.tenantName = tenantName;
  }
}
