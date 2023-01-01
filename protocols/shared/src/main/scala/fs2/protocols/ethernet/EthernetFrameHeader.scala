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

package fs2.protocols
package ethernet

import scodec.Codec
import scodec.codecs._
import fs2.interop.scodec._

import com.comcast.ip4s.MacAddress

/** Header of an ethernet frame as captured in a pcap file.
  */
case class EthernetFrameHeader(
    destination:       MacAddress,
    source:            MacAddress,
    ethertypeOrLength: Int
) {
  def length:    Option[Int] = if (ethertypeOrLength <= 1500) Some(ethertypeOrLength) else None
  def ethertype: Option[Int] = if (ethertypeOrLength > 1500) Some(ethertypeOrLength) else None
}

object EthernetFrameHeader {
  // format: off
  implicit val codec: Codec[EthernetFrameHeader] = {
    ("destination" | Ip4sCodecs.macAddress) ::
    ("source"      | Ip4sCodecs.macAddress) ::
    ("ethertype"   | uint16)
  }.as[EthernetFrameHeader]
  // format: on

  val sdecoder: StreamDecoder[EthernetFrameHeader] = StreamDecoder.once(codec)
}
