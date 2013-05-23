/*
* Copyright 2001-2013 Artima, Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.scalatest

import org.scalautils.Equality
import org.scalautils.StringNormalizations._
import SharedHelpers._

class ListShouldContainOneOfLogicalOrSpec extends Spec with Matchers {
  
  val invertedStringEquality =
    new Equality[String] {
      def areEqual(a: String, b: Any): Boolean = a != b
    }
  
  val invertedListOfStringEquality = 
    new Equality[List[String]] {
      def areEqual(a: List[String], b: Any): Boolean = a != b
    }
  
  val upperCaseStringEquality =
    new Equality[String] {
      def areEqual(a: String, b: Any): Boolean = a.toUpperCase == b
    }
  
  val upperCaseListOfStringEquality = 
    new Equality[List[String]] {
      def areEqual(a: List[String], b: Any): Boolean = a.map(_.toUpperCase) == b
    }
  
  val fileName: String = "ListShouldContainOneOfLogicalOrSpec.scala"
  
  object `a List` {
    
    val fumList: List[String] = List("fum")
    val toList: List[String] = List("to")
    
    object `when used with (contain oneOf (...) or contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (newContain newOneOf ("fee", "fie", "foe", "fum") or newContain newOneOf("fie", "fee", "fum", "foe"))
        fumList should (newContain newOneOf ("fee", "fie", "foe", "fam") or newContain newOneOf("fie", "fee", "fum", "foe"))
        fumList should (newContain newOneOf ("fee", "fie", "foe", "fum") or newContain newOneOf("fie", "fee", "fam", "foe"))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newOneOf ("fee", "fie", "foe", "fam") or newContain newOneOf ("happy", "birthday", "to", "you"))
        }
        checkMessageStackDepth(e1, Resources("didNotContainOneOfElements", fumList, "\"fee\", \"fie\", \"foe\", \"fam\"") + ", and " + Resources("didNotContainOneOfElements", fumList, "\"happy\", \"birthday\", \"to\", \"you\""), fileName, thisLineNumber - 2)
      }

      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or newContain newOneOf ("fie", "fee", "fum", "foe"))
        fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or (newContain newOneOf ("fie", "fee", "fum", "foe")))
        }
        checkMessageStackDepth(e1, Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\"") + ", and " + Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\""), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\"") + ", and " + Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\""), fileName, thisLineNumber - 2)
        (fumList should (newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM ") or newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM "))) (after being lowerCased and trimmed, after being lowerCased and trimmed)
      }
    }
    
    object `when used with (equal (...) and contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (equal (fumList) or newContain newOneOf("fie", "fee", "fum", "foe"))
        fumList should (equal (toList) or newContain newOneOf("fie", "fee", "fum", "foe"))
        fumList should (equal (fumList) or newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (equal (toList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", fumList, toList) + ", and " + Resources("didNotContainOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (equal (fumList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        fumList should (equal (toList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        fumList should (equal (fumList) or newContain newOneOf ("fie", "fee", "fum", "foe"))
        val e1 = intercept[TestFailedException] {
          fumList should (equal (toList) or (newContain newOneOf ("fie", "fee", "fum", "foe")))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", fumList, toList) + ", and " + Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\""), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (equal (toList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (equal (fumList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (equal (toList) or newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (equal (fumList) or newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", fumList, fumList) + ", and " + Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\""), fileName, thisLineNumber - 2)
        (fumList should (equal (toList) or newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM "))) (decided by invertedListOfStringEquality, after being lowerCased and trimmed)
      }
    }
    
    object `when used with (legacyEqual (...) and contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (legacyEqual (fumList) or newContain newOneOf("fie", "fee", "fum", "foe"))
        fumList should (legacyEqual (toList) or newContain newOneOf("fie", "fee", "fum", "foe"))
        fumList should (legacyEqual (fumList) or newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (legacyEqual (toList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", fumList, toList) + ", and " + Resources("didNotContainOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (legacyEqual (fumList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        fumList should (legacyEqual (toList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        fumList should (legacyEqual (fumList) or newContain newOneOf ("fie", "fee", "fum", "foe"))
        val e1 = intercept[TestFailedException] {
          fumList should (legacyEqual (toList) or (newContain newOneOf ("fie", "fee", "fum", "foe")))
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", fumList, toList) + ", and " + Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\""), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (legacyEqual (fumList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality)
        (fumList should (legacyEqual (toList) or newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality)
        (fumList should (legacyEqual (fumList) or newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (legacyEqual (toList) or newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotEqual", fumList, toList) + ", and " + Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\""), fileName, thisLineNumber - 2)
        (fumList should (legacyEqual (fumList) or newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM "))) (after being lowerCased and trimmed)
      }
    }

    object `when used with (contain oneOf (...) and legacyEqual (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (newContain newOneOf("fie", "fee", "fum", "foe") or legacyEqual (fumList))
        fumList should (newContain newOneOf("FEE", "FIE", "FOE", "FUM") or legacyEqual (fumList))
        fumList should (newContain newOneOf("fie", "fee", "fum", "foe") or legacyEqual (toList))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or legacyEqual (toList))
        }
        checkMessageStackDepth(e1, Resources("didNotContainOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\"") + ", and " + Resources("didNotEqual", fumList, toList), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or legacyEqual (fumList))
        fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or legacyEqual (fumList))
        fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or legacyEqual (toList))
        val e1 = intercept[TestFailedException] {
          fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or legacyEqual (toList))
        }
        checkMessageStackDepth(e1, Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\"") + ", and " + Resources("didNotEqual", fumList, toList), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or legacyEqual (fumList))) (decided by upperCaseStringEquality)
        (fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or legacyEqual (fumList))) (decided by upperCaseStringEquality)
        (fumList should (newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or legacyEqual (toList))) (decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (newContain newOneOf ("fie", "fee", "fum", "foe") or legacyEqual (toList))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("didNotContainOneOfElements", fumList, "\"fie\", \"fee\", \"fum\", \"foe\"") + ", and " + Resources("didNotEqual", fumList, toList), fileName, thisLineNumber - 2)
        (fumList should (newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM ") or legacyEqual (fumList))) (after being lowerCased and trimmed)
      }
    }
    
    object `when used with (not contain oneOf (...) and not contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or not newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        fumList should (not newContain newOneOf ("fee", "fie", "foe", "fum") or not newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        fumList should (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or not newContain newOneOf("fie", "fee", "fum", "foe"))
        val e1 = intercept[TestFailedException] {
          fumList should (not newContain newOneOf ("fee", "fie", "foe", "fum") or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        }
        checkMessageStackDepth(e1, Resources("containedOneOfElements", fumList, "\"fee\", \"fie\", \"foe\", \"fum\"") + ", and " + Resources("containedOneOfElements", fumList, "\"fee\", \"fie\", \"foe\", \"fum\""), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (not newContain newOneOf ("fee", "fie", "foe", "fum") or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        fumList should (not newContain newOneOf ("fee", "fie", "foe", "fum") or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        fumList should (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        }
        checkMessageStackDepth(e1, Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\"") + ", and " + Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (not newContain newOneOf ("fee", "fie", "foe", "fum") or not newContain newOneOf ("fee", "fie", "foe", "fum"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (not newContain newOneOf ("fee", "fie", "foe", "fum") or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        (fumList should (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or not newContain newOneOf ("fee", "fie", "foe", "fum"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM") or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\"") + ", and " + Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
        (fumList should (newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM ") or newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUM "))) (after being lowerCased and trimmed, after being lowerCased and trimmed)
      }
    }
    
    object `when used with (not equal (...) and not contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (not equal (toList) or not newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        fumList should (not equal (fumList) or not newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        fumList should (not equal (toList) or not newContain newOneOf("fie", "fee", "fum", "foe"))
        val e1 = intercept[TestFailedException] {
          fumList should (not equal (fumList) or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        }
        checkMessageStackDepth(e1, Resources("equaled", fumList, fumList) + ", and " + Resources("containedOneOfElements", fumList, "\"fee\", \"fie\", \"foe\", \"fum\""), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (not equal (toList) or not newContain newOneOf ("fie", "fee", "fum", "foe"))
        fumList should (not equal (fumList) or not newContain newOneOf ("fie", "fee", "fum", "foe"))
        fumList should (not equal (toList) or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        val e2 = intercept[TestFailedException] {
          fumList should (not equal (fumList) or (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM")))
        }
        checkMessageStackDepth(e2, Resources("equaled", fumList, fumList) + ", and " + Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (not equal (fumList) or not newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (not equal (toList) or not newContain newOneOf ("fie", "fee", "fum", "foe"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        (fumList should (not equal (fumList) or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (not equal (toList) or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by invertedListOfStringEquality, decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("equaled", fumList, toList) + ", and " + Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
        (fumList should (not newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUU ") or not newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUU "))) (after being lowerCased and trimmed, after being lowerCased and trimmed)
      }
    }
    
    object `when used with (not be (...) and not contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        fumList should (not be (toList) or not newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        fumList should (not be (fumList) or not newContain newOneOf("FEE", "FIE", "FOE", "FUM"))
        fumList should (not be (toList) or not newContain newOneOf("fee", "fie", "foe", "fum"))
        val e1 = intercept[TestFailedException] {
          fumList should (not be (fumList) or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        }
        checkMessageStackDepth(e1, Resources("wasEqualTo", fumList, fumList) + ", and " + Resources("containedOneOfElements", fumList, "\"fee\", \"fie\", \"foe\", \"fum\""), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = upperCaseStringEquality
        fumList should (not be (toList) or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        fumList should (not be (fumList) or not newContain newOneOf ("fee", "fie", "foe", "fum"))
        fumList should (not be (toList) or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))
        val e1 = intercept[TestFailedException] {
          fumList should (not be (fumList) or (not newContain newOneOf ("FEE", "FIE", "FOE", "FUM")))
        }
        checkMessageStackDepth(e1, Resources("wasEqualTo", fumList, fumList) + ", and " + Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (fumList should (not be (toList) or not newContain newOneOf ("fee", "fie", "foe", "fum"))) (decided by upperCaseStringEquality)
        (fumList should (not be (fumList) or not newContain newOneOf ("fee", "fie", "foe", "fum"))) (decided by upperCaseStringEquality)
        (fumList should (not be (toList) or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality)
        val e1 = intercept[TestFailedException] {
          (fumList should (not be (fumList) or not newContain newOneOf ("FEE", "FIE", "FOE", "FUM"))) (decided by upperCaseStringEquality)
        }
        checkMessageStackDepth(e1, Resources("wasEqualTo", fumList, fumList) + ", and " + Resources("containedOneOfElements", fumList, "\"FEE\", \"FIE\", \"FOE\", \"FUM\""), fileName, thisLineNumber - 2)
        (fumList should (not newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUU ") or not newContain newOneOf (" FEE ", " FIE ", " FOE ", " FUU "))) (after being lowerCased and trimmed, after being lowerCased and trimmed)
      }
    }
    
  }
  
  object `collection of Lists` {
    
    val list1s: Vector[List[Int]] = Vector(List(1), List(1), List(1))
    val lists: Vector[List[Int]] = Vector(List(1), List(1), List(2))
    val nils: Vector[List[Int]] = Vector(Nil, Nil, Nil)
    val listsNil: Vector[List[Int]] = Vector(List(1), List(1), Nil)
    val hiLists: Vector[List[String]] = Vector(List("hi"), List("hi"), List("hi"))
    val toLists: Vector[List[String]] = Vector(List("to"), List("to"), List("to"))
    
    def allErrMsg(index: Int, message: String, lineNumber: Int, left: Any): String = 
      "'all' inspection failed, because: \n" +
      "  at index " + index + ", " + message + " (" + fileName + ":" + (lineNumber) + ") \n" +
      "in " + left
    
    object `when used with (contain oneOf (...) and contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (newContain newOneOf (3, 2, 1) or newContain newOneOf (1, 3, 4))
        all (list1s) should (newContain newOneOf (3, 2, 5) or newContain newOneOf (1, 3, 4))
        all (list1s) should (newContain newOneOf (3, 2, 1) or newContain newOneOf (2, 3, 4))
        
        atLeast (2, lists) should (newContain newOneOf (3, 1, 5) or newContain newOneOf (1, 3, 4))
        atLeast (2, lists) should (newContain newOneOf (3, 6, 5) or newContain newOneOf (1, 3, 4))
        atLeast (2, lists) should (newContain newOneOf (3, 1, 5) or newContain newOneOf (8, 3, 4))
        
        val e1 = intercept[TestFailedException] {
          all (lists) should (newContain newOneOf (6, 7, 8) or newContain newOneOf (1, 3, 4))
        }
        checkMessageStackDepth(e1, allErrMsg(2, "List(2) did not contain one of (6, 7, 8), and List(2) did not contain one of (1, 3, 4)", thisLineNumber - 2, lists), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        
        all (hiLists) should (newContain newOneOf ("ho") or newContain newOneOf ("he"))
        all (hiLists) should (newContain newOneOf ("hi") or newContain newOneOf ("he"))
        all (hiLists) should (newContain newOneOf ("ho") or newContain newOneOf ("hi"))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (newContain newOneOf ("hi") or newContain newOneOf ("hi"))
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) did not contain one of (\"hi\"), and List(hi) did not contain one of (\"hi\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (newContain newOneOf ("ho") or newContain newOneOf ("ho"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        (all (hiLists) should (newContain newOneOf ("hi") or newContain newOneOf ("ho"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        (all (hiLists) should (newContain newOneOf ("ho") or newContain newOneOf ("hi"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (newContain newOneOf ("hi") or newContain newOneOf ("hi"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) did not contain one of (\"hi\"), and List(hi) did not contain one of (\"hi\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (be (...) and contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (be (List(1)) or newContain newOneOf (1, 3, 4))
        all (list1s) should (be (List(2)) or newContain newOneOf (1, 3, 4))
        all (list1s) should (be (List(1)) or newContain newOneOf (2, 3, 4))
        
        val e1 = intercept[TestFailedException] {
          all (list1s) should (be (List(2)) or newContain newOneOf (2, 3, 8))
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(1) was not equal to List(2), and List(1) did not contain one of (2, 3, 8)", thisLineNumber - 2, list1s), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        
        all (hiLists) should (be (List("hi")) or newContain newOneOf ("he"))
        all (hiLists) should (be (List("ho")) or newContain newOneOf ("he"))
        all (hiLists) should (be (List("hi")) or newContain newOneOf ("hi"))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (be (List("ho")) or newContain newOneOf ("hi"))
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) was not equal to List(ho), and List(hi) did not contain one of (\"hi\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (be (List("hi")) or newContain newOneOf ("ho"))) (decided by invertedStringEquality)
        (all (hiLists) should (be (List("ho")) or newContain newOneOf ("ho"))) (decided by invertedStringEquality)
        (all (hiLists) should (be (List("hi")) or newContain newOneOf ("hi"))) (decided by invertedStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (be (List("ho")) or newContain newOneOf ("hi"))) (decided by invertedStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) was not equal to List(ho), and List(hi) did not contain one of (\"hi\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (not contain oneOf (...) and not contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (not newContain newOneOf (3, 2, 8) or not newContain newOneOf (8, 3, 4))
        all (list1s) should (not newContain newOneOf (1, 2, 8) or not newContain newOneOf (8, 3, 4))
        all (list1s) should (not newContain newOneOf (3, 2, 8) or not newContain newOneOf (8, 3, 1))
        
        val e1 = intercept[TestFailedException] {
          all (lists) should (not newContain newOneOf (2, 6, 8) or not newContain newOneOf (2, 3, 4))
        }
        checkMessageStackDepth(e1, allErrMsg(2, "List(2) contained one of (2, 6, 8), and List(2) contained one of (2, 3, 4)", thisLineNumber - 2, lists), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        
        all (hiLists) should (not newContain newOneOf ("hi") or not newContain newOneOf ("hi"))
        all (hiLists) should (not newContain newOneOf ("ho") or not newContain newOneOf ("hi"))
        all (hiLists) should (not newContain newOneOf ("hi") or not newContain newOneOf ("h0"))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (not newContain newOneOf ("ho") or not newContain newOneOf ("ho"))
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) contained one of (\"ho\"), and List(hi) contained one of (\"ho\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (not newContain newOneOf ("hi") or not newContain newOneOf ("hi"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        (all (hiLists) should (not newContain newOneOf ("ho") or not newContain newOneOf ("hi"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        (all (hiLists) should (not newContain newOneOf ("hi") or not newContain newOneOf ("ho"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (not newContain newOneOf ("ho") or not newContain newOneOf ("ho"))) (decided by invertedStringEquality, decided by invertedStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) contained one of (\"ho\"), and List(hi) contained one of (\"ho\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
    
    object `when used with (not be (...) and not contain oneOf (...)) syntax` {
      
      def `should do nothing if valid, else throw a TFE with an appropriate error message` {
        all (list1s) should (not be (List(2)) or not newContain newOneOf (8, 3, 4))
        all (list1s) should (not be (List(1)) or not newContain newOneOf (8, 3, 4))
        all (list1s) should (not be (List(2)) or not newContain newOneOf (8, 3, 1))
        
        val e1 = intercept[TestFailedException] {
          all (list1s) should (not be (List(1)) or not newContain newOneOf (2, 3, 1))
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(1) was equal to List(1), and List(1) contained one of (2, 3, 1)", thisLineNumber - 2, list1s), fileName, thisLineNumber - 2)
      }
      
      def `should use the implicit Equality in scope` {
        implicit val ise = invertedStringEquality
        
        all (hiLists) should (not be (List("ho")) or not newContain newOneOf ("hi"))
        all (hiLists) should (not be (List("hi")) or not newContain newOneOf ("hi"))
        all (hiLists) should (not be (List("ho")) or not newContain newOneOf ("ho"))
        
        val e1 = intercept[TestFailedException] {
          all (hiLists) should (not be (List("hi")) or not newContain newOneOf ("ho"))
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) was equal to List(hi), and List(hi) contained one of (\"ho\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
      
      def `should use an explicitly provided Equality` {
        (all (hiLists) should (not be (List("ho")) or not newContain newOneOf ("hi"))) (decided by invertedStringEquality)
        (all (hiLists) should (not be (List("hi")) or not newContain newOneOf ("hi"))) (decided by invertedStringEquality)
        (all (hiLists) should (not be (List("ho")) or not newContain newOneOf ("ho"))) (decided by invertedStringEquality)
        
        val e1 = intercept[TestFailedException] {
          (all (hiLists) should (not be (List("hi")) or not newContain newOneOf ("ho"))) (decided by invertedStringEquality)
        }
        checkMessageStackDepth(e1, allErrMsg(0, "List(hi) was equal to List(hi), and List(hi) contained one of (\"ho\")", thisLineNumber - 2, hiLists), fileName, thisLineNumber - 2)
      }
    }
  }
}
