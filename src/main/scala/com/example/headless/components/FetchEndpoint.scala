package com.example.headless.components

import com.raquo.airstream.web.FetchStream
import com.raquo.laminar.api.L._
import io.circe.Decoder
import io.circe.parser.decode

/** Reusable helper for fetching a JSON array endpoint and decoding it into a Table. */
object FetchEndpoint {

  /** Fetches a JSON array from `url`, decodes each element as `A`, and converts to a Table.
    *
    * Updates `target` with `Success(table)` on success or `Error(message)` on failure. Returns a fire-and-forget
    * EventStream that callers should bind to start the fetch.
    */
  def fetch[A: Decoder](
      url: String,
      target: Var[FetchState[Table]],
      caption: String,
      headers: List[String],
      toRow: A => List[String],
      take: Int = 10
  ): EventStream[Unit] =
    FetchStream
      .get(url)
      .map { responseText =>
        decode[List[A]](responseText) match {
          case Right(items) =>
            target.set(FetchState.Success(Table(Some(caption), headers, items.take(take).map(toRow))))
          case Left(err) => target.set(FetchState.Error(err.getMessage))
        }
      }
      .recover { case err: Throwable =>
        target.set(FetchState.Error(err.getMessage))
        Some(())
      }
}
