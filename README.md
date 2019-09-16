# buildit-crawler

The project is a typical Spring Boot web application with:

## /crawl REST endpoint:

curl -X POST "http://localhost:8082/api/v0/crawl" -H "accept: application/json" -H "Content-Type: application/json" -d "http://your.url.goes.here"

and 

## Swagger UI for REST test and review facilitation:

http://localhost:8082/api/v0/swagger-ui.html

**NOTE**: Subdomains are treated as external addresses.

Example of JSON response:

```
{
  "url": "http://www.milliondollarhomepage.com/",
  "internalLinks": [
    "./index_files/index.html",
    "http://www.milliondollarhomepage.com/faq.php",
    "http://www.milliondollarhomepage.com/testimonials.php",
    "http://www.milliondollarhomepage.com/pixellist.php",
    "http://www.milliondollarhomepage.com/buy.php",
    "http://www.milliondollarhomepage.com/http//:www.phoneoutletstore.com",
    "http://www.milliondollarhomepage.com/blog.php",
    "http://www.milliondollarhomepage.com/press.php"
  ],
  "externalLinks": [
    "http://www.pokahrooms.com/sign-up-bonus",
    "http://www.milionpixelspage.com/",
    "http://www.clearandsmoothskin.com/",
    "http://www.philadelphia-nmfn.com/",
    "http://vepkenez.com/",
    "http://www.googadollars.com/",
    "http://www.ccsports-online.co.uk/",
    "http://www.job-secrets-revealed.com/",
    "http://earnmoremoneyonline.com/easymoney.html",
    "http://www.robsavino.com/",
    "http://www.ochimapictures.com/",
    "http://skylinkweather.com/"
  ],
  "staticLinks": [
    "./index_files/image-map(1).png",
    "./index_files/logo-tm.gif",
    "./index_files/neg.gif",
    "./index_files/image-map.png",
    "./index_files/toptag2top.gif"
  ]
}
```

## Possible extensions:
- more manageable parsing rules (HTML elements and attributes should not be hardcoded in the service but configured somehow externally)
- multithreaded website crawling (Fork-Join Java framework ???)
- possible bugs fixed as currently the crawler follows some obvious scenarios
- avoid web requests overloading (and hence be blocked by vendor) - some smart intermediate waiting between requests ...
- ...
