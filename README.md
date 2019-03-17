# Tickets4Sale

Coding Challenge for Vakantie Discounter and Otravo (Spilberg)

## Installation

The project was built with [Leiningen](https://leiningen.org/).
To check that everything compiles, do
```shell
% lein check
```

## Backend
The backend can be run in either CLI or server mode.

### CLI Mode
Tell the backend to run the CLI mode, providing a query date, show date and 
and path to a CSV file to initialize the show store:

```shell
% lein run --run cli --query-date 2018-08-01 --show-date 2018-08-15 --data ./test/fixtures/some.csv
```
The options to the CLI are as follows:
```
                              (default)
-r, --run cli-or-server       cli                        CLI or server mode
-q, --query-date YYYY-MM-DD   2019-03-17 (today)         Query Date
-s, --show-date YYYY-MM-DD                               Show Date
-d, --data /path/to/file.csv  ./test/fixtures/shows.csv  Path to CSV file with initial data
```

If you choose to build the Überjar,
```shell
% lein uberjar
```

Then it can be run as a stand-alone Java spp:
```shell
% java -jar ./target/uberjar/tickets4sale-0.0.1-US1-standalone.jar --run cli --query-date 2018-08-01 --show-date 2018-08-15 --data ./test/fixtures/some.csv
```

Either way, the output will always be a show report as a JSON string:
```json
{
  "inventory": [
    {
      "genre": "COMEDY",
      "shows": [
        {
          "title": "BEAUX' STRATAGEM, THE",
          "tickets-left": 100,
          "tickets-available": 10,
          "status": "open for sale"
        },
        {
          "title": "LIGHTS! CAMERA! IMPROVISE!",
          "tickets-left": 100,
          "tickets-available": 10,
          "status": "open for sale"
        }
      ]
    },
    {
      "genre": "DRAMA",
      "shows": [
        {
          "title": "WINTER'S TALE, THE",
          "tickets-left": 100,
          "tickets-available": 10,
          "status": "open for sale"
        },
        {
          "title": "HARRY POTTER AND THE CURSED CHILD",
          "tickets-left": 100,
          "tickets-available": 10,
          "status": "open for sale"
        }
      ]
    },
    {
      "genre": "MUSICAL",
      "shows": [
        {
          "title": "WICKED ",
          "tickets-left": 100,
          "tickets-available": 10,
          "status": "open for sale"
        },
        {
          "title": "COMMITMENTS, THE",
          "tickets-left": 100,
          "tickets-available": 10,
          "status": "open for sale"
        }
      ]
    }
  ]
}

```

### Server Mode

Run the server:
```shell
lein run --run server --query-date 2018-08-01 --show-date 2018-08-15 --data ./test/fixtures/shows.csv
```

The ticket status report can be accessed via the URL pattern:
```
http://localhost:8080/ticket-status/2018-08-02
```
... there the stub is the show date based on the pattern "YYYY-MM-DD"

## Frontend
Coming soon.

## License

Copyright © 2019 Aventine Solutions

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
