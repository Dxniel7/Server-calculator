<?php
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $data = json_decode(file_get_contents('php://input'), true);
    $operation = $data['operation'];
    // Realiza el cálculo de la operación
    $result = eval('return ' . $operation . ';');

    $response = array('result' => $result);
    echo json_encode($response);
}
?>
