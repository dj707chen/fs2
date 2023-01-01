/*
 * Copyright (c) 2013 Functional Streams for Scala
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

// Adapted from scodec-protocols, licensed under 3-clause BSD

package fs2.protocols.mpeg
package transport

import scodec.Err
import scodec.bits.BitVector

sealed abstract class DemultiplexerError {
  def toMpegError: MpegError
}

object DemultiplexerError {

  case class Discontinuity(
      last:                   ContinuityCounter,
      current:                ContinuityCounter,
      adaptationFieldControl: Int
  ) extends DemultiplexerError
      with MpegError {
    def message     =
      s"pid discontinuity: $last to $current with adaptation field control $adaptationFieldControl"
    def toMpegError = this
  }

  case class Decoding(data: BitVector, decodingError: Err) extends DemultiplexerError {
    def message     = s"decoding error ($decodingError) while decoding ${data.toHex}"
    def toMpegError = MpegError.Decoding(data, decodingError)
  }
}
