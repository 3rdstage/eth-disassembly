
Aggregation Queries
====

* Transaction count by minute

    ```javascript
    db.transactions.aggregate([
      { $group: {
          _id: {
            year: {"$year": "$at"},
            month: {"$month": "$at"},
            day: {"$dayOfMonth": "$at"},
            hour: {$hour: "$at"},
            min: {$minute: "$at"}
          },
          trs: { $sum: 1 }
        }
      }
    ])
    ```