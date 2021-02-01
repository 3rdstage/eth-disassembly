
// References
//   - https://docs.mongodb.com/manual/tutorial/write-scripts-for-the-mongo-shell/
//   - https://docs.mongodb.com/manual/core/schema-validation/
//   - https://docs.mongodb.com/manual/reference/operator/query/jsonSchema/
//   - http://json-schema.org/understanding-json-schema/

db.getSiblingDB('eth')

db.accounts.createIndex(
  { addr: 1 }, 
  { unique: true})

db.runCommand({
  collMod: "accounts", 
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["addr", "is_contr"],
      properties: {
        addr: {
          title: "address",
          bsonType: "string",
          pattern: "0x[0-9A-Fa-f]{1,40}"
        },
        is_contr: {
          title: "is contract",
          bsonType: "boolean",
        }
      }
    }
  },
  validationAction: "warn"
})