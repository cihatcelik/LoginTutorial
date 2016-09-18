<?php

    require("config_tutorial.php");

    if (!empty($_POST)) {

        $response = array("error" => FALSE);

        $query = "SELECT * FROM kullanicilar WHERE email = :email";

        $query_params = array(
            ':email' => $_POST['email']
        );

        try {
            $stmt = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }

        catch (PDOException $ex) {
            $response["error"] = true;
            $response["message"] = "Database Connection Error!!!";
            die(json_encode($response));
        }

        $validated_info = false;
        $login_ok = false;
        $email = $_POST['email'];

        $row = $stmt->fetch();

        if ($row) {
            $login_ok = true;
        }

        if ($login_ok == true) {
            $response["error"] = false;
            $response["message"] = "Login successful!";
            $response["user"]["name"] = $row["name"];
            $response["user"]["email"] = $row["email"];
            $response["user"]["uid"] = $row["unique_id"];
            $response["user"]["created_at"] = $row["created_at"];
            die(json_encode($response));

        } else {
            $response["error"] = true;
            $response["message"] = "Parameter Error!!";
            die(json_encode($response));
        }

    } else {

        echo 'test';
    }

?>