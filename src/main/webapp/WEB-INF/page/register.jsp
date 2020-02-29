<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, shrink-to-fit=no">
	<title>UserCreation</title>
	<link rel="stylesheet" href="/resource/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="/resource/css/registration/Pretty-Registration-Form.css">
	<link rel="stylesheet" href="/resource/css/registration/styles.css">
</head>

<body>
<div class="row register-form">
	<div class="col-md-8 offset-md-2">
		<form class="custom-form">
			<h1>Register Form</h1>
			<div class="form-row form-group">
				<div class="col-sm-3 label-column"><label class="col-form-label" for="name-input-field">Name </label></div>
				<div class="col-sm-3 input-column"><input class="form-control" type="text"></div>
				<div class="col-sm-2 label-column"><label class="col-form-label" for="name-input-field">Surname </label></div>
				<div class="col-sm-3 input-column"><input class="form-control" type="text"></div>
			</div>
			<div class="form-row form-group">
				<div class="col-sm-3 label-column"><label class="col-form-label" for="email-input-field">Email </label></div>
				<div class="col-sm-8 input-column"><input class="form-control" type="email"></div>
			</div>
			<div class="form-row form-group">
				<div class="col-sm-3 label-column"><label class="col-form-label" for="pawssword-input-field">Password </label></div>
				<div class="col-sm-8 input-column"><input class="form-control" type="password"></div>
			</div>
			<div class="form-row form-group">
				<div class="col-sm-3 label-column"><label class="col-form-label" for="repeat-pawssword-input-field">Repeat Password </label></div>
				<div class="col-sm-8 input-column"><input class="form-control" type="password"></div>
			</div>
			<div class="form-row form-group">
				<div class="col-sm-3 label-column"><label class="col-form-label">Role </label></div>
				<div class="col-sm-1 label-column"><label class="col-form-label"></label></div>
				<div class="form-check form-check-inline">
					<input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
					<label class="form-check-label" for="inlineCheckbox1">Student</label>
				</div>
				<div class="form-check form-check-inline">
					<input class="form-check-input" type="checkbox" id="inlineCheckbox2" value="option2">
					<label class="form-check-label" for="inlineCheckbox2">Responsible</label>
				</div>
				<div class="form-check form-check-inline">
					<input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="option2">
					<label class="form-check-label" for="inlineCheckbox3">Teacher</label>
				</div>
				<div class="form-check form-check-inline">
					<input class="form-check-input" type="checkbox" id="inlineCheckbox4" value="option4">
					<label class="form-check-label" for="inlineCheckbox4">Secretary</label>
				</div>
			</div>
			<!--<div class="form-row form-group">
				<div class="col-sm-3 label-column"><label class="col-form-label" for="dropdown-input-field">Dropdown </label></div>
				<div class="col-sm-8 input-column">
					<div class="dropdown"><button class="btn btn-light dropdown-toggle" data-toggle="dropdown" aria-expanded="false" type="button">Dropdown </button>
						<div class="dropdown-menu" role="menu"><a class="dropdown-item" role="presentation" href="#">First Item</a><a class="dropdown-item" role="presentation" href="#">Second Item</a><a class="dropdown-item" role="presentation" href="#">Third Item</a></div>
					</div>
				</div>
			</div>-->
			<button class="btn btn-light submit-button" type="button">Create</button></form>
	</div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script src="/resource/bootstrap/js/bootstrap.min.js"></script>
</body>

</html>