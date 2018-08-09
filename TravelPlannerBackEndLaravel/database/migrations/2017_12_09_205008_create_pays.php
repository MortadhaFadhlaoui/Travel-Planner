<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreatePays extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
         Schema::create('pays', function (Blueprint $table) {
            $table->increments('id');
            $table->string('nom_pays');
            $table->integer('etat')->nullable();
            $table->integer('pack_id')->unsigned();;
            $table->foreign('pack_id')->references('id')->on('packs')->onDelete('cascade');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
