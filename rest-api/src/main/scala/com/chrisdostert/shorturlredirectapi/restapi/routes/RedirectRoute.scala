package com.chrisdostert.shorturlredirectapi.restapi.routes

import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.Directives._
import com.chrisdostert.shorturlredirectapi.restapi.json.JsonSupport
import com.chrisdostert.shorturlsvcsdk.ShortUrlSvcSdk

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

private[restapi]
class RedirectRoute(
  jsonSupport: JsonSupport,
  shortUrlSvcSdk: ShortUrlSvcSdk
) {

  val route =
    get {
      pathPrefix(Segment) {
        shortUrlId =>
          redirect(
            Await.result(
              shortUrlSvcSdk
                .getShortUrlWithId(shortUrlId)
                .map(shortUrlView => Uri(shortUrlView.target.toString)),
              20 seconds
            ),
            StatusCodes.Found
          )
      }
    }

}
