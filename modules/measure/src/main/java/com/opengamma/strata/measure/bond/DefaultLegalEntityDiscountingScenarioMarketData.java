/*
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.measure.bond;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.atomic.AtomicReferenceArray;

import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.TypedMetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableConstructor;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.light.LightMetaBean;

import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.data.scenario.ScenarioMarketData;
import java.util.Set;
import org.joda.beans.Property;

/**
 * The default market data for products based on repo and issuer curves, used for calculation across multiple scenarios.
 * <p>
 * This uses a {@link LegalEntityDiscountingMarketDataLookup} to provide a view on {@link ScenarioMarketData}.
 */
@BeanDefinition(style = "light")
final class DefaultLegalEntityDiscountingScenarioMarketData
    implements LegalEntityDiscountingScenarioMarketData, ImmutableBean, Serializable {

  /**
   * The lookup.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final LegalEntityDiscountingMarketDataLookup lookup;
  /**
   * The market data.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final ScenarioMarketData marketData;
  /**
   * The cache of single scenario instances.
   */
  private final transient AtomicReferenceArray<LegalEntityDiscountingMarketData> cache;  // derived

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance based on a lookup and market data.
   * <p>
   * The lookup provides the mapping to find the correct repo and issuer curves.
   * The curves are in the market data.
   *
   * @param lookup  the lookup
   * @param marketData  the market data
   * @return the rates market view
   */
  static DefaultLegalEntityDiscountingScenarioMarketData of(
      LegalEntityDiscountingMarketDataLookup lookup,
      ScenarioMarketData marketData) {

    return new DefaultLegalEntityDiscountingScenarioMarketData(lookup, marketData);
  }

  @ImmutableConstructor
  private DefaultLegalEntityDiscountingScenarioMarketData(
      LegalEntityDiscountingMarketDataLookup lookup,
      ScenarioMarketData marketData) {

    this.lookup = ArgChecker.notNull(lookup, "lookup");
    this.marketData = ArgChecker.notNull(marketData, "marketData");
    this.cache = new AtomicReferenceArray<>(marketData.getScenarioCount());
  }

  // ensure standard constructor is invoked
  private Object readResolve() {
    return new DefaultLegalEntityDiscountingScenarioMarketData(lookup, marketData);
  }

  //-------------------------------------------------------------------------
  @Override
  public LegalEntityDiscountingScenarioMarketData withMarketData(ScenarioMarketData marketData) {
    return DefaultLegalEntityDiscountingScenarioMarketData.of(lookup, marketData);
  }

  //-------------------------------------------------------------------------
  @Override
  public int getScenarioCount() {
    return marketData.getScenarioCount();
  }

  @Override
  public LegalEntityDiscountingMarketData scenario(int scenarioIndex) {
    LegalEntityDiscountingMarketData current = cache.get(scenarioIndex);
    if (current != null) {
      return current;
    }
    return cache.updateAndGet(
        scenarioIndex,
        v -> v != null ? v : lookup.marketDataView(marketData.scenario(scenarioIndex)));
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code DefaultLegalEntityDiscountingScenarioMarketData}.
   */
  private static final TypedMetaBean<DefaultLegalEntityDiscountingScenarioMarketData> META_BEAN =
      LightMetaBean.of(DefaultLegalEntityDiscountingScenarioMarketData.class, MethodHandles.lookup());

  /**
   * The meta-bean for {@code DefaultLegalEntityDiscountingScenarioMarketData}.
   * @return the meta-bean, not null
   */
  public static TypedMetaBean<DefaultLegalEntityDiscountingScenarioMarketData> meta() {
    return META_BEAN;
  }

  static {
    MetaBean.register(META_BEAN);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  @Override
  public TypedMetaBean<DefaultLegalEntityDiscountingScenarioMarketData> metaBean() {
    return META_BEAN;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the lookup.
   * @return the value of the property, not null
   */
  @Override
  public LegalEntityDiscountingMarketDataLookup getLookup() {
    return lookup;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data.
   * @return the value of the property, not null
   */
  @Override
  public ScenarioMarketData getMarketData() {
    return marketData;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DefaultLegalEntityDiscountingScenarioMarketData other = (DefaultLegalEntityDiscountingScenarioMarketData) obj;
      return JodaBeanUtils.equal(lookup, other.lookup) &&
          JodaBeanUtils.equal(marketData, other.marketData);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(lookup);
    hash = hash * 31 + JodaBeanUtils.hashCode(marketData);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("DefaultLegalEntityDiscountingScenarioMarketData{");
    buf.append("lookup").append('=').append(lookup).append(',').append(' ');
    buf.append("marketData").append('=').append(JodaBeanUtils.toString(marketData));
    buf.append('}');
    return buf.toString();
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
