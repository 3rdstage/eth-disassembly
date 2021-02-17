package thirdstage.utils;

import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberComparisonUtils{


  public static boolean isPositive(BigInteger a) {
    return greaterThanOrAtLeast(a, BigInteger.ZERO, false);
  }

  public static Optional<Boolean> isPositiveSafe(BigInteger a) {
    return greaterThanOrAtLeastSafe(a, BigInteger.ZERO, false);
  }

  public static boolean isPositiveOrZero(BigInteger a) {
    return greaterThanOrAtLeast(a, BigInteger.ZERO, true);
  }

  public static Optional<Boolean> isPositiveOrZeroSafe(BigInteger a){
    return greaterThanOrAtLeastSafe(a, BigInteger.ZERO, true);

  }

  public static boolean greater(BigInteger a, BigInteger b) {
    return greaterThanOrAtLeast(a, b, false);
  }

  public static Optional<Boolean> greaterSafe(BigInteger a, BigInteger b){
    return greaterThanOrAtLeastSafe(a, b, false);
  }

  // isPositive
  // isPositiveOrZero
  // isNegative
  // isNegativeOrZero
  // greater
  // greaterOrEqaul
  // less
  // lessOrEqaul


  // BigInteger, BigInteger
  // BigInteger, int
  // BigInteger, long
  // BigInteger, BigDecimal
  // int, BigInteger
  // long, BigInteger
  // BigDecimal, BigInteger

  // BigDecimal, BigDecimal
  // BigDecimal, int
  // BigDecimal, long
  // int, BigDecimal
  // long, BigDecimal


  private static boolean greaterThanOrAtLeast(BigInteger a, BigInteger b, boolean permitsEqual) {

    if(a == null) {
      if(b == null) return permitsEqual;
      else return false;
    }else {
      if(b == null) return true;
      else return greaterThanOrAtLeastSafe(a, b, permitsEqual).get();
    }
  }

  private static Optional<Boolean> greaterThanOrAtLeastSafe(BigInteger a, BigInteger b, boolean permitsEqual){
    if(a == null || b == null) return Optional.empty();

    switch(a.compareTo(b)) {
    case 1: return Optional.of(Boolean.TRUE);
    case 0: return Optional.of(permitsEqual ? Boolean.TRUE : Boolean.FALSE);
    case -1: return Optional.of(Boolean.FALSE);
    default : return Optional.empty(); // never reachable semantically but to avoid compile-time syntatic error
    }
  }


}
