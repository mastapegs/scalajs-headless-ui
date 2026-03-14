package com.example.headless

import com.raquo.airstream.core.Signal
import com.raquo.airstream.ownership.ManualOwner

trait SignalHelpers {

  protected def signalNow[A](signal: Signal[A]): A = {
    val owner = new ManualOwner
    var value = Option.empty[A]
    signal.foreach(v => value = Some(v))(owner)
    owner.killSubscriptions()
    value.get
  }
}
