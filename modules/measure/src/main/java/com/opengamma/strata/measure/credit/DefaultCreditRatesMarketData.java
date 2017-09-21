/*
 * Copyright (C) 2017 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.measure.credit;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.TypedMetaBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableConstructor;
import org.joda.beans.gen.PropertyDefinition;
import org.joda.beans.impl.light.LightMetaBean;

import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.data.MarketData;
import com.opengamma.strata.pricer.credit.CreditRatesProvider;
import java.util.Set;
import org.joda.beans.Property;

/**
 * The default market data for products based on credit, discount and recovery rate curves.
 * <p>
 * This uses a {@link CreditRatesMarketDataLookup} to provide a view on {@link MarketData}.
 */
@BeanDefinition(style = "light")
final class DefaultCreditRatesMarketData
    implements CreditRatesMarketData, ImmutableBean, Serializable {

  /**
   * The lookup.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final CreditRatesMarketDataLookup lookup;
  /**
   * The market data.
   */
  @PropertyDefinition(validate = "notNull", overrideGet = true)
  private final MarketData marketData;
  /**
   * The credit rates provider.
   */
  private final transient CreditRatesProvider creditRatesProvider; // derived

  //-------------------------------------------------------------------------
  static DefaultCreditRatesMarketData of(CreditRatesMarketDataLookup lookup, MarketData marketData) {
    return new DefaultCreditRatesMarketData(lookup, marketData);
  }

  @ImmutableConstructor
  private DefaultCreditRatesMarketData(CreditRatesMarketDataLookup lookup, MarketData marketData) {
    this.lookup = ArgChecker.notNull(lookup, "lookup");
    this.marketData = ArgChecker.notNull(marketData, "marketData");
    this.creditRatesProvider = lookup.creditRatesProvider(marketData);
  }

  // ensure standard constructor is invoked
  private Object readResolve() {
    return new DefaultCreditRatesMarketData(lookup, marketData);
  }

  //-------------------------------------------------------------------------
  @Override
  public CreditRatesMarketData withMarketData(MarketData marketData) {
    return DefaultCreditRatesMarketData.of(lookup, marketData);
  }

  //-------------------------------------------------------------------------
  @Override
  public CreditRatesProvider creditRatesProvider() {
    return creditRatesProvider;
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code DefaultCreditRatesMarketData}.
   */
  private static final TypedMetaBean<DefaultCreditRatesMarketData> META_BEAN =
      LightMetaBean.of(DefaultCreditRatesMarketData.class, MethodHandles.lookup());

  /**
   * The meta-bean for {@code DefaultCreditRatesMarketData}.
   * @return the meta-bean, not null
   */
  public static TypedMetaBean<DefaultCreditRatesMarketData> meta() {
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
  public TypedMetaBean<DefaultCreditRatesMarketData> metaBean() {
    return META_BEAN;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the lookup.
   * @return the value of the property, not null
   */
  @Override
  public CreditRatesMarketDataLookup getLookup() {
    return lookup;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the market data.
   * @return the value of the property, not null
   */
  @Override
  public MarketData getMarketData() {
    return marketData;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DefaultCreditRatesMarketData other = (DefaultCreditRatesMarketData) obj;
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
    buf.append("DefaultCreditRatesMarketData{");
    buf.append("lookup").append('=').append(lookup).append(',').append(' ');
    buf.append("marketData").append('=').append(JodaBeanUtils.toString(marketData));
    buf.append('}');
    return buf.toString();
  }

  //-------------------------- AUTOGENERATED END --------------------------
}
