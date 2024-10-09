<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('users', function (Blueprint $table) {
            $table->id();
            $table->integer('pt_id')->unique()->nullable();
            $table->string('fullname')->nullable();
            $table->string('name');
            $table->string('email')->unique();
            $table->string('password');
            $table->string('phone')->nullable();
            $table->string('company')->nullable();
            $table->string('address')->nullable();
            $table->string('photo')->nullable();
            $table->integer('status');
            $table->dateTime('created_on')->nullable();
            $table->tinyInteger('is_admin')->default(0);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};