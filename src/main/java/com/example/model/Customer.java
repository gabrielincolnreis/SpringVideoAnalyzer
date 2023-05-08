package com.example.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@DynamoDBTable(tableName = "Customer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @DynamoDBHashKey(attributeName = "CustomerID")
    @DynamoDBAutoGeneratedKey
    private Long CustomerID;

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String email;
}