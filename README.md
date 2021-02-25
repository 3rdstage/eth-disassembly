
Aggregation Queries
====

* Transaction count by minute

    ```javascript
    db.transactions.aggregate([{
      $group: {
        _id: {
          $dateFromParts: {
            'year': {"$year": "$at"},
            'month': {"$month": "$at"},
            'day': {"$dayOfMonth": "$at"},
            'hour': {$hour: "$at"},
            'minute': {$minute: "$at"}
          }
        },
        trs: { $sum: 1 }
      }
    }]
    ```
  