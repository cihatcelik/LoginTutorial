<?php

    require("config_tutorial.php");

    if (!empty($_POST)) {

        $response = array(
            "error" => FALSE
        );

        $query = " SELECT 1 FROM kullanicilar WHERE email = :email";

        //now lets update what :user should be
        $query_params = array(
            ':email' => $_POST['email']
        );

        try {
            $stmt = $db->prepare($query);
            $result = $stmt->execute($query_params);
        }
        catch (PDOException $ex) {

            $response["error"] = TRUE;
            $response["message"] = "Database Connection Error!!";
            die(json_encode($response));
        }

        $row = $stmt->fetch();
        if ($row) {

            $response["error"] = TRUE;
            $response["message"] = "I'm sorry, this email is already in use";
            die(json_encode($response));
        } else {
            $query = "INSERT INTO kullanicilar ( unique_id, name, email, encrypted_password, created_at ) VALUES ( :uuid, :name, :email, :encrypted_password, NOW() ) ";

            $query_params = array(
                ':uuid' => uniqid('', true),
                ':name' => $_POST['name'],
                ':email' => $_POST['email'],
                ':encrypted_password' => password_hash($_POST['password'], PASSWORD_DEFAULT) // encrypted password
            );

            try {
                $stmt = $db->prepare($query);
                $result = $stmt->execute($query_params);
            }

            catch (PDOException $ex) {
                $response["error"] = TRUE;
                $response["message"] = "Query Error!!";
                die(json_encode($response));
            }

            $response["error"] = FALSE;
            $response["message"] = "Register successful!";
            echo json_encode($response);
        }

    } else {
        echo 'Test';
    }

?>